'####################################################################################################################################################################
'Script Description		: Script to invoke the Test Summary Details from ALM
'Test Tool			: VAPI-XP
'Pre Requisite			: outlook, Word must be available in the local machine
'Test Tool Settings		: N.A
'Author				: Gilead TCOE SQA Automation Team
'Date Created			: 18/09/2017
'####################################################################################################################################################################
'Option Explicit
'####################################################################################################################################################################
'Class Description   		: EmailUtility class
'Author				: Gilead TCOE SQA Automation Team
'Date Created			: 18/09/2017
'####################################################################################################################################################################

Class EmailUtility
	
	'Status Related Variables

	Private intPassCount,intFailCount,intNotCompleted,intNoRun,strStatus

	'Report File Variables

	private strOverAllSummaryFile,strReportFiles,strResultsPath

	'Total Tests Counter and Test information Related Variables

	Private intTotalDuration,intTotalVmsused,intTotalTests,others,strHostName,strRunStartTime,strRunEndTime,strVmsused
	
	'Class property Variables

	Private m_objCurrentTestSet,m_strRelativePath,m_strUserName
	
	'###################################################################################################################
	Public Property Let RelativePath(strRelativePath)
		m_strRelativePath = strRelativePath
	End Property
	'###################################################################################################################

	'###################################################################################################################
	Public Property Let UserName(strUserName)
		m_strUserName = strUserName 
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTestSet(objCurrentTestSet)
		Set m_objCurrentTestSet = objCurrentTestSet
	End Property
	'###################################################################################################################


'#####################################################################################################################################################################
'Function to Capture Test results from ALM and Send via Outlook
'#####################################################################################################################################################################

public Function SendTestResults()

	call GetTestDetailsFromTestSet(m_objCurrentTestSet)

	'Mail related variables

	Dim strMailTo,strMailCC,strSubject,strReportsUploadPath,strBody

	'Application related variables
	
	Dim strBrowser,strEnvironment,strProject

	strMailTo = ReadVariablefromProperties(m_strRelativePath+"\Global Settings.properties","To_Mail")
	strMailCC = ReadVariablefromProperties(m_strRelativePath+"\Global Settings.properties","CC_Mail")
	strSubject = ReadVariablefromProperties(m_strRelativePath+"\Global Settings.properties","Summary_Subject")
	strEnvironment = ReadVariablefromProperties(m_strRelativePath+"\Global Settings.properties","Summary_Environment")
	strBrowser = ReadVariablefromProperties(m_strRelativePath+"\Global Settings.properties","Summary_Browser")
	strProject = ReadVariablefromProperties(m_strRelativePath+"\Global Settings.properties","ProjectName")
	strReportsUploadPath="<p>Reports can be found at :"&m_objCurrentTestSet.Name&"</p>"
	strBody="<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}</style></head><body><div><div>Hello Team,</div></br><div></div>"&_
	""& strProject &" test execution is completed. Below is the quick tabular representation of execution status : </div></br><table><tr ><td >Executed by</td><td>"&_
	""& m_strUserName&"</td></tr><tr ><td>Environment</td><td>"&strEnvironment&"</td></tr><tr><td >Browser</td><td>"&strBrowser&"</td></tr><tr><td>Executed on </td><td>"&_
	""& Date&"</td></tr><tr><td >Total TC's #</td><td>"&intTotalTests&"</td></tr><tr><td >Passed #</td><td>"&intPassCount&"</td></tr><tr><td >Failed #</td><td>"&intFailCount&""&_
	"</td></tr><tr><td>Not Completed </td><td>"&intNotCompleted&"</td></tr><tr><td>No Run</td><td>"&intNoRun&"</td></tr><tr><td>Start Time</td><td>"&strRunStartTime&""&_
	"</td></tr><tr><td>End Time</td><td>"&strRunEndTime&"</td></tr><tr><td>Total Duration</td><td>"&calculateTime(intTotalDuration)&"</td></tr></table></br><p>ALM test"&_
	"set Path       : "&m_objCurrentTestSet.TestSetFolder.Path&"\"&"</p>"&strReportsUploadPath&"<p>VDIs used :"&strVmsused&"</p> </br><div>Thanks,</div><div>"&_
	"SQA Automation</div></body></html>"

	SendTestResults = SendMail(strMailTo,strMailCC,strSubject,strBody,strReportFiles)

End Function

'#####################################################################################################################################################################
'Function to Capture Test results from ALM
'#####################################################################################################################################################################

Function GetTestDetailsFromTestSet(CurrentTestSet)

	'Test Factory Related variables

	Dim objTsTestFac,objTsTestList

	'Run Factory Related variables

	Dim objTsRunFac,objTsRunList,intRunListCount
	
	'Test information related variables

	Dim intTestListCount,strTestResults,strTestvm,strTestName,intTestduration,strTestStatus,intSNo,booStartTimeFlag,arrHostName

	'File Related variables

	Dim objFso,objMyFile

	strOverAllSummaryFile = m_strRelativePath & "\Email_Utility\" & CurrentTestSet.Name & ".html"
	Set objFso = CreateObject("Scripting.FileSystemObject")
	if objFso.FileExists(m_strRelativePath & "\Email_Utility\" & CurrentTestSet.Name & ".html") then
            objFso.DeleteFile m_strRelativePath & "\Email_Utility\" & CurrentTestSet.Name & ".html",true
	end if
	if objFso.FileExists(m_strRelativePath & "\Email_Utility\" & CurrentTestSet.Name & "_RegressionExecution.pdf") then
            objFso.DeleteFile m_strRelativePath & "\Email_Utility\" & CurrentTestSet.Name & "_RegressionExecution.pdf",true
	end if
	
	strTestResults=""
	booStartTimeFlag = true
	intTotalTests=0
	intPassCount=0
	intFailCount=0
	intTestduration=0
	intNoRun = 0
	intNotCompleted = 0
	intSNo = 1
	strTestvm=""
	strVmsused=""
	StrRunStartTime="0:0:0"
	strRunEndTime="0:0:0"
	
	Set objTsTestFac = CurrentTestSet.TSTestFactory
        Set objTsTestList = objTsTestFac.NewList("")
	intTestListCount = objTsTestList.Count

	If intTestListCount > 0 Then
		For i = 1 To intTestListCount-1   'index will start from 1 for Test List in ALM
	        	Set objTsRunFac = objTsTestList.Item(i).RunFactory
	                Set objTsRunList = objTsRunFac.NewList("")
			intRunListCount = objTsRunList.Count
			if intRunListCount > 0 then
				strTestName = objTsTestList.Item(i).TestName
				intTestduration = objTsRunList.Item(intRunListCount).Field("RN_Duration")
				strTestvm = objTsRunList.Item(intRunListCount).Field("RN_HOST")
				strTestStatus = objTsRunList.Item(intRunListCount).Field("RN_STATUS")
				Temp = objTsRunList.Item(intRunListCount).Field("RN_EXECUTION_TIME")
				
				if booStartTimeFlag = true then
					strRunEndTime = Temp
					StrRunStartTime = DateAdd("n",-intTestduration,CDate(strRunEndTime))
					booStartTimeFlag = False
				Else

					if (DateDiff("s",CDate(strRunEndTime),CDate(Temp))>0) then
				
						strRunEndTime = Temp

					End If

				End IF
				
				If Len(strHostName) = 0 then 
					strHostName = strTestvm
				Else
					strHostName = strHostName&","&strTestvm
				End If
				
				intTotalDuration = DateDiff("n",StrRunStartTime,strRunEndTime)
			Else
				strTestName = objTsTestList.Item(i).TestName
				strTestStatus = "No Run"
			End If

			If StrComp(strTestStatus,"Passed",1)=0 Then
				intPassCount=intPassCount+1
				strTestStatus="<td style='color:green'>"&strTestStatus&"</td>"
			ElseIf StrComp(strTestStatus,"Failed",1)=0 Then
				intFailCount=intFailCount+1
				strTestStatus="<td style='color:red'>"&strTestStatus&"</td>"	
			ElseIf Instr(strTestStatus,"No Run") then	
				strTestStatus="<td >"&strTestStatus&"</td>"
				intNoRun=intNoRun+1
			Else
				strTestStatus="<td >"&strTestStatus&"</td>"
				intNotCompleted=intNotCompleted+1
			End IF
		
			strTestResults = strTestResults& "<tr class='content' style='font-size:10px'><td>" & intSNo & "</td><td>" & strTestName & "</td>" & strTestStatus &"</tr>"			
			intSNo = intSNo + 1	
			intTotalTests=intTotalTests+1

		Next

		arrHostName = Split(strHostName, ",")
		intTotalVmsused=0
		
		For intLoop = 0 To UBound(arrHostName)
		        If InStr( ucase(strVmsused), ucase(arrHostName(intLoop))) = 0 Then
	 			strVmsused = arrHostName(intLoop) & ",</br>" & strVmsused  
				intTotalVmsused=intTotalVmsused+1
			End If
		Next

		call fnHTMLCreation

		Set objMyFile = objFso.OpenTextFile(strOverAllSummaryFile, 8)
		
		objMyFile.WriteLine("</table><table><tr ><td style='border-left:1px solid;width:20%;;font-size:10px'>Total Tests:"&intTotalTests&"</td><td style='border"&_
		"-left:1px solid;width:54%;font-size:10px'> Passed :"&intPassCount&"</td><td style='font-size:10px'>Failed :"&intFailCount&"</td></tr></table><table style='text-align: Left;"&_
		" ' id='header'><thead><tr class='heading'><th style='width:20%;font-size:10px'>S.No</th><th style='width:54%;font-size:10px'>Test Name</th>"&_
		"<th style='font-size:10px'>Status</th></tr>"&strTestResults&"</table> </body></html>")
    
		objMyFile.Close

		strResultsPath = m_strRelativePath&"\Email_Utility"		

		call fnPDFConversion

	Else
		Msgbox "Test is not found under the testset " & m_objCurrentTestSet.Name & " in ALM"
	End If

    	'Set nothing to the created objects
	Set objTsTestFac = Nothing
	Set objTsTestList = Nothing
	Set objTsRunFac = Nothing
	Set objTsRunList = Nothing
	Set objMyFile = Nothing
	Set objFso = Nothing

End Function


'#####################################################################################################################################################################
'Function to Create HTML File
'#####################################################################################################################################################################

Sub fnHTMLCreation()
    
	Dim objMyFile,objFso,intLoop,strTestSetName,objSysInfo,strTestLogHeader
	
	Set objFso = CreateObject("Scripting.FileSystemObject")
	Set objMyFile = objFso.CreateTextFile(strOverAllSummaryFile, True)
	objMyFile.Close
    
    	'Initial Value pls don't change it

	intLoop = 1
	strTestSetName = ""

    	Set objSysInfo = CreateObject( "WinNTSystemInfo" )
	strComputerName = objSysInfo.ComputerName
	Set objMyFile = objFso.OpenTextFile(strOverAllSummaryFile, 8)  ' 8 - Append Mode

	strTestLogHeader = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Automation Execution Results</title>" & _
	GetThemeCss() & _
	GetJavascriptFunctions() & _
	"<table id='header' style='text-align: left;font-size:10px'><tr ><td style='width:20%'>Date & Time </td><td style='width:41%'>" & Now &"</td><td style='width:25%'>"&_ 
	"No of VM's</td><td style='width:14%'>"&intTotalVmsused&"</td></tr><tr ><td style='border:none;border-left:1px solid;border-bottom:1px solid;'>Test Set Path  "&_
	"</td><td style='border:none;border-left:1px solid;border-bottom:1px solid'>" & m_objCurrentTestSet.TestSetFolder.Path&"\"&m_objCurrentTestSet.Name & "</td></tr>"&_
	"</table></head><body style='border: 1px solid #000000;'>"  
                                  
	objMyFile.WriteLine (strTestLogHeader)
	objMyFile.Close

	Set objMyFile = Nothing
	Set objFso = Nothing
	Set objSysInfo = Nothing

End Sub


'#####################################################################################################################################################################
'Function to get CSS Theme
'#####################################################################################################################################################################

Function GetThemeCss()
        
	Dim strThemeCss

        strThemeCss = "<style type='text/css'>body{font-family: Verdana, Geneva, sans-serif;" & _
                        "text-align: center;" & _
                        "border: 1px solid #000000;" & _
                        "}small {font-size: 0.7em;}" & _
                      "table {border: 1px solid #000000;width: 100%;text-align: left;" & _
                        "border-collapse: collapse;" & _
                        "border-spacing: 1px;" & _
                        "width: 100%;" & _
                        "margin-left: auto;" & _
                        "margin-right: auto;}"
        
	strThemeCss = strThemeCss & "tr.heading {" & _
                            "" & _
                            "border: 1px solid #000000;" & _
                            "font-weight: bold;text-align: center;}" & _
                           "tr.subheading {" & _
                            "border: 1px solid #000000;" & _
                            "color: #000000 " & ";" & _
                            "font-weight: bold;" & _
                            "font-size: 0.9em;" & _
                            "text-align: justify;}"

	strThemeCss = strThemeCss & "tr.section{" & _
                            "border: 1px solid #000000;color:#000000;cursor: pointer;" & _
                            "font-weight: bold;font-size: 0.9em;text-align: justify;}" & _
                           "tr.subsection {cursor: pointer;}" & _
                           "tr.content {color:#000000;font-size: 0.9em;" & _
                            "display: table-row;border: 1px solid #000000;}" & _
                           "td {padding: 4px;border: 1px solid #000000;text-align:inherit;" & _
                            "word-wrap: break-word;" & _
                            "max-width: 450px;}" & _
                           "th {padding: 4px;border: 1px solid #000000;text-align:inherit;" & _
                            "word-break: break-all;" & _
                            "max-width: 450px;}td.justified {" & _
                            "text-align: Left;" & _
                           "}td.pass {font-weight: bold;color: green;}"
        
	strThemeCss = strThemeCss & "td.fail {font-weight: bold;color: red;}" & _
                           "td.screenshot {font-weight: bold;color: navy;}" & _
                           "td.done {font-weight: bold;color: black;}" & _
                            "td.debug {" & _
                            "font-weight: bold;" & _
                            "color: blue;" & _
                           "}td.warning {font-weight: bold;color: orange;}</style>"
        
        GetThemeCss = strThemeCss

End Function


'#####################################################################################################################################################################
'Function to get Javascript Functions
'#####################################################################################################################################################################

Function GetJavascriptFunctions()

	Dim strJavascriptFunctions

	strJavascriptFunctions = "<script>function toggleMenu(objID) {if (!document.getElementById) return;" & _
                                "var ob = document.getElementById(objID).style;" & _
                                "if(ob.display === 'none') {try {" & _
                                      "ob.display='table-row-group';" & _
                                     "} catch(ex) {ob.display='block';}" & _
                                    "}else {" & _
                                     "ob.display='none';" & _
                                    "}}function toggleSubMenu(objId) {" & _
                                    "for(i=1; i<10000; i++) {" & _
                                     "var ob = document.getElementById(objId.concat(i));" & _
                                     "if(ob === null) {" & _
                                      "break;}" & _
                                     "if(ob.style.display === 'none') {" & _
                                      "try { " & _
                                       "ob.style.display='table-row';" & _
                                      "} catch(ex) {" & _
                                       "ob.style.display='block';" & _
                                      "}" & _
                                     "}" & _
                                     "else {" & _
                                      "ob.style.display='none';" & _
                                     "}" & _
                                    "}" & _
                                   "}" & _
                                  "</script>"
    
	GetJavascriptFunctions = strJavascriptFunctions

End Function

'#####################################################################################################################################################################
'Function to send Mail
'#####################################################################################################################################################################

public Function SendMail(SendTo,CC, strSubject, Body, Attachment)

	Dim objOutlook,ObjMail,strAttachment,x

	Set objOutlook = CreateObject("Outlook.Application")
	Set ObjMail=objOutlook.CreateItem(0)
    
	ObjMail.to=SendTo
	ObjMail.Subject=strSubject
	ObjMail.CC = CC	
	ObjMail.HTMLBody=Body
	strAttachment=Split(Attachment,";")

	for each x in strAttachment
		If (Attachment <> "") Then
			ObjMail.Attachments.Add(x)
		End If
	Next
    
    	ObjMail.Send

	If Err Then
		WScript.Echo "SendMail Failed:" & Err.Description
		strStatus = "Failed"
	Else
	   	strStatus = "Passed"
	End IF

	Set ObjMail = Nothing
	Set objOutlook = Nothing

    	SendMail = strStatus

End Function

'#####################################################################################################################################################################
'Function to Calculate Time difference
'#####################################################################################################################################################################

Function calculateTime(SecondsDifference)

	Dim intHours, intMinutes, intSeconds

	' calculates whole hours (like a div operator)

	intHours = SecondsDifference \ 60

	' calculates the remaining number of seconds

	intMinutes  = SecondsDifference Mod 60

	' calculates the whole number of minutes in the remaining number of seconds
	'minutes = SecondsDifference \ 60

	' calculates the remaining number of seconds after taking the number of minutes
	
	intSeconds = 0

	'hms = TimeSpan.FromSeconds(SecondsDifference)
	'h = hms.Hours.ToString
	'm = hms.Minutes.ToString
	's = hms.Seconds.ToString
	'Hours=Hours.ToString.PadLeft(2, "0"c)
	'Minutes=Minutes.ToString.PadLeft(2, "0"c)
	'Seconds=Seconds.ToString.PadLeft(2, "0"c)

	calculateTime = intHours & ":" & intMinutes & ":" & intSeconds

End Function


'#####################################################################################################################################################################
'Function to Convert Word to PDF
'#####################################################################################################################################################################

Sub fnPDFConversion()

	'Winword related variables	

	Dim strDoccopy,objWord,objDocument,objSelection,intSrcpgnumber,objWord1,objDocument1,objSelection1

	'File related variables

	Dim objFilesys

	set objFilesys=CreateObject("Scripting.FileSystemObject")

	strDoccopy=strResultsPath&"\summary1.docx"

	If objFilesys.FileExists(strResultsPath&"\summary.docx") Then
		objFilesys.CopyFile strResultsPath&"\summary.docx",strDoccopy 	
	End If
	
	Set objWord = CreateObject( "Word.Application" )
	objWord.Visible = False

	Set objDocument = objWord.Documents.Open(strResultsPath &"\"&m_objCurrentTestSet.Name&".html",ConfirmConversions=True, Format=wdOpenFormatAuto)
	Set objSelection= objWord.Selection
	intSrcpgnumber=objDocument.ComputeStatistics(2)

	Set objWord1 = CreateObject("Word.Application")
	objWord1.visible=False
	Set objDocument1 = objWord.Documents.open(strDoccopy)
	Set objSelection1= objWord.Selection
         
	objSelection.wholestory
	objSelection.copy
	objSelection1.PasteAndFormat(wdPasteDefault)

	objDocument1.SaveAs strResultsPath&"\"&m_objCurrentTestSet.Name&"_RegressionExecution.pdf",17

	If (strReportFiles <> "") Then
		strReportFiles=strReportFiles&";"&strResultsPath&"\"&m_objCurrentTestSet.Name&"_RegressionExecution.pdf"
	else
		strReportFiles=strResultsPath&"\"&m_objCurrentTestSet.Name&"_RegressionExecution.pdf"
	End if

	objword.Application.Quit wdDoNotSaveChanges
	objWord1.Application.Quit wdDoNotSaveChanges

	set objword=Nothing
	Set objWord1 = Nothing
	Set filesys = Nothing
	Set objDocument = Nothing
	Set objDocument1 = Nothing
	set objSelection = Nothing
	set objSelection1 = Nothing

End Sub

'#####################################################################################################################################################################
'Function to Read Value from Properties File
'#####################################################################################################################################################################

public Function ReadVariablefromProperties(PropertyFilePath,strvariable)

	Dim booFlag,fso,fConFile,strLen,VariableValue,strConfigLine,EqualSignPosition
	
	set fso = CreateObject("Scripting.FileSystemObject")

	Set fConFile = fso.OpenTextFile(PropertyFilePath)

	do while(NOT fConFile.AtEndOfStream)
		strConfigLine = fConFile.ReadLine
		strConfigLine = TRIM(strConfigLine)
		
		IF ((INSTR(1,strConfigLine,"#",1) <> 1) AND (INSTR(1,strConfigLine,strvariable,1) <> 0))THEN
			booFlag = True
			EqualSignPosition = INSTR(1,strConfigLine,"=",1)
			strLen = LEN(strConfigLine)
			VariableValue = TRIM(Mid(strConfigLine, EqualSignPosition + 1, strLen - EqualSignPosition))
			ReadVariablefromProperties= VariableValue
	Exit do
		End If
	Loop

	if booFlag = False then
		msgbox " "&strvariable&" is not found in the property file "&PropertyFilePath
	End IF

	Set fso = nothing
	Set fConFile = nothing

End Function

End Class