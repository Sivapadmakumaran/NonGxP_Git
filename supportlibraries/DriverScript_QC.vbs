'#######################################################################################################################
'Script Description		: Script to invoke the corresponding test script in the local machine
'Test Tool/Version		: VAPI-XP
'Test Tool Settings		: N.A.
'Application Automated	: Mercury Tours
'Author					: Cognizant
'Date Created			: 26/12/2012
'#######################################################################################################################
'Option Explicit	'Forcing Variable declarations

'#######################################################################################################################
'Class Description   	: DriverScript class
'Author					: Cognizant
'Date Created			: 09/11/2012
'#######################################################################################################################
Class DriverScript
	
	Private m_strRelativePath,m_strWorkspaceParentFolder,m_strWorkspaceFolder
	Private m_blnDebug, m_objCurrentTestSet, m_objCurrentTSTest, m_objCurrentRun
	Private m_strReportPath
	Private m_strCurrentTestParentFolder, m_strCurrentTestCase, m_strCurrentTestInstance
	Private m_strTestStatus
	
	'###################################################################################################################
	Public Property Let RelativePath(strRelativePath)
		m_strRelativePath = strRelativePath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let ParentFolder(strWorkspaceParentFolder)
		m_strWorkspaceParentFolder = strWorkspaceParentFolder
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let WorkspaceFolder(strWorkspaceFolder)
		m_strWorkspaceFolder = strWorkspaceFolder
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let Debug(blnDebug)
		m_blnDebug = blnDebug
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTestSet(objCurrentTestSet)
		Set m_objCurrentTestSet = objCurrentTestSet
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTSTest(objCurrentTSTest)
		Set m_objCurrentTSTest = objCurrentTSTest
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentRun(objCurrentRun)
		Set m_objCurrentRun = objCurrentRun
	End Property
	'###################################################################################################################
	
	
	'###################################################################################################################
	'Function Description   : Function to drive the test execution
	'Input Parameters 		: None
	'Return Value    		: None
	'Author					: Cognizant
	'Date Created			: 11/10/2012
	'###################################################################################################################
   	Public Function DriveTestExecution()
		InvokeBatchFile()
		UploadResultsToQc()
		
		If m_strTestStatus = 0 Then
			DriveTestExecution = "Passed"
		Else
			DriveTestExecution = "Failed"
		End If
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub InvokeBatchFile()
		Dim strScriptPath
		strScriptPath = m_strRelativePath & "\allocator\RunTest.bat"
		
		m_strCurrentTestParentFolder = m_objCurrentTSTest.Test.Field("TS_SUBJECT").Name
		m_strCurrentTestCase = m_objCurrentTSTest.TestName
		m_strCurrentTestInstance = "Instance" & m_objCurrentTSTest.Instance
		
		Dim strCurrentTestDescription
		TDConnection.IgnoreHTMLFormat = True
		strCurrentTestDescription = m_objCurrentTSTest.Test.Field("TS_DESCRIPTION")
		If strCurrentTestDescription = "" Then
			strCurrentTestDescription = "SKIP"
		End If
		
		m_strReportPath = SetUpTempResultFolder()
		
		Dim strCommandLineArguments
		strCommandLineArguments = """" & m_strReportPath & """ """ & m_strCurrentTestParentFolder &_
									""" """ & m_strCurrentTestCase & """ """ & m_strCurrentTestInstance &_
									""" """ & strCurrentTestDescription & """ """ & m_strWorkspaceParentFolder & """ """ & m_strWorkspaceFolder & """" 
			
									
		
		m_strTestStatus = XTools.run(strScriptPath, strCommandLineArguments, -1, True)
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetTestParameters()
		Dim objParamValueFactory: Set objParamValueFactory = m_objCurrentTSTest.ParameterValueFactory
		Dim lstParams: Set lstParams = objParamValueFactory.NewList("")
		Dim objParam
		Dim strIterationMode, intStartIteration, intEndIteration
		Dim strExecutionMode, strDeviceName
		Dim strBrowser, strBrowserVersion, strPlatform
		Dim strMobileToolName, strMobileExecutionPlatform
		
		For Each objParam in lstParams
		
			Select Case objParam.Name
				Case "IterationMode":
					strIterationMode = GetValue(objParam)
				Case "StartIteration":
					intStartIteration = GetValue(objParam)
				Case "EndIteration":
					intEndIteration = GetValue(objParam)
				Case "ExecutionMode":
					strExecutionMode = GetValue(objParam)
				Case "MobileToolName":
					strMobileToolName = GetValue(objParam)
				Case "MobileExecutionPlatform":
					strMobileExecutionPlatform = GetValue(objParam)
				Case "DeviceName":
					strDeviceName = GetValue(objParam)
				Case "Browser":
					strBrowser = GetValue(objParam)
				Case "BrowserVersion":
					strBrowserVersion = GetValue(objParam)
				Case "Platform":
					strPlatform = GetValue(objParam)
			End Select
		Next
		
		GetTestParameters = strIterationMode & " " & intStartIteration & " " & intEndIteration & " " &_
								strExecutionMode & " " & strMobileToolName & " " & strMobileExecutionPlatform & " """ & strDeviceName & """ " &_
								strBrowser & " " & strBrowserVersion & " " & strPlatform
		
		Set objParam = Nothing
		Set lstParams = Nothing
		Set objParamValueFactory = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetValue(objParam)
		Dim strValue
		strValue =RemoveHTMLStrip(objParam.DefaultValue)
		If strValue = "" Then
			strValue = "SKIP"
		End If
		
		GetValue = strValue
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function SetUpTempResultFolder()
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		
		Dim strTempResultPath	'Using the Windows temp folder to store the results before uploading to QC
		strTempResultPath = objFso.GetSpecialFolder(2) & "\Run_mm-dd-yyyy_hh-mm-ss_XX"

		'Create Temp results folder if it does not exist
		If Not objFso.FolderExists (strTempResultPath) Then
			objFso.CreateFolder(strTempResultPath)
		End If
		
		strTempResultPath = strTempResultPath & "\" & m_strCurrentTestCase & "_" & m_objCurrentTSTest.Instance
		
		'Delete test case level result folder if it already exists
		If objFso.FolderExists(strTempResultPath) Then
			objFso.DeleteFolder(strTempResultPath)
			
			'Wait until the folder is successfully deleted
			Do While(1)
				If Not objFso.FolderExists(strTempResultPath) Then
					Exit Do
				End If
			Loop
		End If
		
		'Create separate folder with the test case name
		objFso.CreateFolder(strTempResultPath)
		
		SetUpTempResultFolder = strTempResultPath
		
		'Release all objects
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub UploadResultsToQc()
		TDOutput.Print ""
		TDOutput.Print "Uploading result files..."
		
		Dim strReportName
		strReportName = m_strCurrentTestParentFolder &_
						"_" & m_strCurrentTestCase &_
						"_" & m_strCurrentTestInstance
		
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		
		If objFso.FolderExists(m_strReportPath & "\Excel Results") Then
			AttachFileToTestRun m_strReportPath & "\Excel Results\" & strReportName & ".xls"
		End If
		
		If objFso.FolderExists(m_strReportPath & "\HTML Results") Then
			AttachFileToTestRun m_strReportPath & "\HTML Results\" & strReportName & ".html"
		End If
		
		If objFso.FolderExists(m_strReportPath & "\Perfecto Results") Then
			If objFso.FileExists(m_strReportPath & "\Perfecto Results\" & strReportName & ".pdf") Then
				AttachFileToTestRun m_strReportPath & "\Perfecto Results\" & strReportName & ".pdf"
			End If
		End If
		
		AttachFolderToTestRun m_strReportPath & "\Screenshots"
		
		If objFso.FolderExists(m_strReportPath & "\Datatables") Then
			AttachFileToTestRun m_strReportPath & "\Datatables\" & m_strCurrentTestParentFolder & ".xls"
			AttachFileToTestRun m_strReportPath & "\Datatables\Common Testdata.xls"
		End If
		
		steplevelupdate()
		
		If m_strTestStatus = 2 Then
			TDOutput.Print "Error occurred while uploading the result files!"
		Else
			TDOutput.Print "Result files uploaded successfully!"
		End If
		
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub AttachFileToTestRun(strFilePath)
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FileExists(strFilePath) Then
			TDOutput.Print "The file to be attached (" & strFilePath & ") is not found!"
			m_strTestStatus = 2	'Any non-zero value is considered as a failure
			Exit Sub
		End If
		Set objFso = Nothing
		
		Dim objFoldAttachments: Set objFoldAttachments = m_objCurrentRun.Attachments
		Dim objFoldAttachment: Set objFoldAttachment = objFoldAttachments.AddItem(Null)
		objFoldAttachment.FileName = strFilePath
		objFoldAttachment.Type = 1
		objFoldAttachment.Post
		
		Set objFoldAttachment = Nothing
		Set objFoldAttachments = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub AttachFolderToTestRun(strFolderPath)
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FolderExists(strFolderPath) Then
			TDOutput.Print "The folder to be attached (" & strFolderPath & ") is not found!"
			m_strTestStatus = 2	'Any non-zero value is considered as a failure
			Exit Sub
		End If
		
		Dim objFolder: Set objFolder = objFso.GetFolder(strFolderPath)
		Dim objFileList: Set objFileList = objFolder.Files
		Dim objFile
		For each objFile in objFileList
			AttachFileToTestRun objFile.Path
		Next
		
		'Release all objects
		Set objFile = Nothing
		Set objFileList = Nothing
		Set objFolder = Nothing
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub steplevelupdate()
	
		'vbs related variables
		Dim objFso,objShell
		'excel related variables
		Dim objExcel,objSheet,objWorkbook,objWorkSheet
		'ALM Related variables
		Dim objStepFactory,objAttachmentFactory,objAttachmentItem
		'Framework related variables
		Dim strResultFolderName,strTestCaseName,strTempFolder,strExcelFileName,blnSheetFlag,intRowCount,i,strResultsFolderPath,strStepLevelFolderName,strHomeFolder
		Dim strStepName,strStepDescription,strStepExpected,strStepActual,strStepStatus,objScreenshotFiles,objFile
	  
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objExcel = CreateObject("Excel.Application")
		Set objShell = CreateObject("WScript.Shell")
		objExcel.visible=False
		blnSheetFlag = False
		strTestCaseName = m_objCurrentTSTest.TestName
		strResultFolderName = strTestCaseName & "_" & m_objCurrentTSTest.Instance
		strExcelFileName = strTestCaseName & "_" & m_objCurrentTSTest.Instance
		strTempFolder = objShell.ExpandEnvironmentStrings("%Temp%") 
		strHomeFolder = objShell.ExpandEnvironmentStrings("%UserProfile%") 
		strResultsFolderPath = strTempFolder&"\"&strResultFolderName
		Set objWorkbook = objExcel.Workbooks.Open(strResultsFolderPath&"\"&strExcelFileName&".xls")
		For Each objSheet In objWorkbook.Sheets
			If objSheet.Name = "ALM Steps" Then
				blnSheetFlag = True
				Exit For
			End If
		Next
		if blnSheetFlag = False then
			MsgBox "Error  - Excel Sheet " & """" & strSheetName & """" & " does not exists in the excel file " & """" & objWorkbook.FullName & """",16,""&_
				"Error - Excel Sheet Not Found"
		Else
			Set objWorkSheet = objWorkbook.Sheets("ALM Steps")
			intRowCount = objWorkSheet.UsedRange.Rows.count
			For i=2 to intRowCount step 1 'First Row Contains Header
				if (Len(objWorkSheet.Cells(i, 5).Value)) then
					strStepName = objWorkSheet.Cells(i, 1).Value
					strStepDescription = objWorkSheet.Cells(i, 2).Value
					strStepExpected = objWorkSheet.Cells(i, 3).Value
					strStepActual = objWorkSheet.Cells(i, 4).Value
					strStepStatus = objWorkSheet.Cells(i, 5).Value
					Set objStepFactory = m_objCurrentRun.StepFactory.AddItem(null)
					objStepFactory.Field("ST_STEP_NAME")=strStepName
					objStepFactory.Field("ST_DESCRIPTION") = strStepDescription
					objStepFactory.Field("ST_EXPECTED") = strStepExpected	
					objStepFactory.Field("ST_ACTUAL") = strStepActual
					objStepFactory.Field("ST_STATUS") = strStepStatus
					objStepFactory.Post
					strStepLevelFolderName = ReadVariablefromProperties(strHomeFolder & "\" & m_strWorkspaceParentFolder & "\" & m_strWorkspaceFolder & "\Global Settings.properties","StepLevelScreenshotsFolderName")
					if(objFso.FolderExists(strResultsFolderPath & "\" & strStepLevelFolderName & " " & (i-1))) then
						Set objScreenshotFiles = objFso.GetFolder(strResultsFolderPath & "\" & strStepLevelFolderName & " " & (i-1)).Files
							if objScreenshotFiles.Count > 0 then
								for each objFile in objScreenshotFiles
									Set objAttachmentFactory = objStepFactory.attachments
									Set objAttachmentItem = objAttachmentFactory.AddItem(null)
									objAttachmentItem.FileName = strResultsFolderPath &"\" & strStepLevelFolderName &" "& (i-1) & "\" & objFile.name
									objAttachmentItem.Type = 1
									objAttachmentItem.Post
									objAttachmentItem.Refresh
								Next
							End If
					End If
					
					'Releasing objects
					Set objStepFactory = Nothing
					Set objScreenshotFiles = Nothing
					Set objFile = Nothing
					Set objAttachmentFactory = Nothing
					Set objAttachmentItem = Nothing
					
				End IF
			Next
		End IF
				
		objExcel.DisplayAlerts = False
		objWorkbook.Close False
		objExcel.Quit
		
		'Releasing objects
		
		Set objFso = nothing
		Set objShell = nothing
		Set objWorkSheet = Nothing
		Set objWorkbook = nothing
		Set objExcel = nothing
		
	End Sub
	
	'###################################################################################################################
	Function RemoveHTMLStrip(gobjsetHTMLParamValues)
        'Strips the HTML tags from gobjsetHTMLParamValues
         Dim objRegExp,strHTMLParameterValues
         Set objRegExp = New Regexp

         objRegExp.IgnoreCase = True
         objRegExp.Global = True
         objRegExp.Pattern = "<(.|\n)+?>"

         'Replace all HTML tag matches with the empty string
         strHTMLParameterValues = objRegExp.Replace(gobjsetHTMLParamValues, "")
                'Replace all < and > with &lt; and &gt;
         strHTMLParameterValues = Replace(strHTMLParameterValues, "<", "&lt;")
         strHTMLParameterValues = Replace(strHTMLParameterValues, ">", "&gt;")
         RemoveHTMLStrip = strHTMLParameterValues    'Return the value of gstrHTMLParameterValues
         Dim regEx
             Set regEx = New RegExp
                 regEx.Global = true
         regEx.IgnoreCase = True
         regEx.Pattern = "\s{2,}"
         RemoveHTMLStrip = Trim(regEx.Replace(RemoveHTMLStrip, " "))

		  Set objRegExp = Nothing
		  Set regEx = Nothing
		End Function
	
	'###################################################################################################################
	
	Function ReadVariablefromProperties(strPropertyFilePath,strVariable)

    Dim blnFlag,objFile,intLen,strVariableValue,strConfigLine,intEqualSignPosition,strVariableInProperties,objFso

    blnFlag = False
    Set objFso = CreateObject("Scripting.FileSystemObject")
    Set objFile = objFso.OpenTextFile(strPropertyFilePath)
    do while(NOT objFile.AtEndOfStream)
       strConfigLine = TRIM(objFile.ReadLine)
       IF ((INSTR(1,strConfigLine,"#",1) <> 1) AND (INSTR(1,strConfigLine,strVariable,1) <> 0))THEN
            intEqualSignPosition = INSTR(1,strConfigLine,"=",1)
            intLen = LEN(strConfigLine)
            strVariableInProperties = TRIM(Mid(strConfigLine, 1, intEqualSignPosition-1))
            if strComp(strVariableInProperties,strVariable,vbtextcompare) = 0 then
               blnFlag = True
               strVariableValue = TRIM(Mid(strConfigLine, intEqualSignPosition + 1, intLen - intEqualSignPosition))
               ReadVariablefromProperties= strVariableValue
               Exit Do
            End IF
       End If
    Loop
    objFile.Close
    Set objFile = nothing
    Set objFso = nothing

    if blnFlag = False then
       MsgBox "Error - " & strvariable & " is not found in the property file " & strPropertyFilePath,16,"Error - Variable Not Found"
       Err.Raise 6005,"Error - Property Not Found","Error - " & strvariable & " is not found in the property file " & strPropertyFilePath
    End If

End Function
	
'#######################################################################################################################
	
End Class
'#######################################################################################################################
