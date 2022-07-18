Dim strworkspacePath,intTotalTests,intPassCount,intFailCount,intAbortedCount,strRunStartTime,strRunEndTime,strOverAllExecutionTime,strSummaryFilePath,ArrHomeFolderPath

Dim strMailto,strMailCC,strSubject,strBody,objfso,strProject,strHomeFolder,objShell,strHostName,objMyFile,objEmailUtility,strEnvironment,strBrowser

strworkspacePath = Wscript.Arguments.Item(0)
intTotalTests = Wscript.Arguments.Item(1)
intPassCount = Wscript.Arguments.Item(2)
intFailCount = Wscript.Arguments.Item(3)
intAbortedCount = Wscript.Arguments.Item(4)
strRunStartTime = Wscript.Arguments.Item(5)
strRunEndTime = Wscript.Arguments.Item(6)
strOverAllExecutionTime = Wscript.Arguments.Item(7)
strsummaryFilePath = Wscript.Arguments.Item(8)

Set objfso = CreateObject("Scripting.FileSystemObject")
Set objShell = CreateObject("WScript.Shell")
strHomeFolderpath = objShell.ExpandEnvironmentStrings("%USERPROFILE%")
ArrHomeFolderPath = Split(strHomeFolderpath,"\")
strHostName = objShell.ExpandEnvironmentStrings("%COMPUTERNAME%")

		Set objMyFile = objfso.OpenTextFile(strworkspacePath & "\Email_Utility\EmailUtility.vbs", 1) ' 1 - For Reading
          
		Execute objMyFile.ReadAll()
          	
		Set objMyFile = Nothing

		Set objEmailUtility = New EmailUtility
          	
		strMailto = objEmailUtility.ReadVariablefromProperties(strworkspacePath+"\Global Settings.properties","To_Mail")

		strMailCC = objEmailUtility.ReadVariablefromProperties(strworkspacePath+"\Global Settings.properties","CC_Mail")

		strSubject = objEmailUtility.ReadVariablefromProperties(strworkspacePath+"\Global Settings.properties","Summary_Subject")

		strProject = objEmailUtility.ReadVariablefromProperties(strworkspacePath+"\Global Settings.properties","ProjectName")

		strEnvironment = objEmailUtility.ReadVariablefromProperties(strworkspacePath+"\Global Settings.properties","Summary_Environment")

		strBrowser = objEmailUtility.ReadVariablefromProperties(strworkspacePath+"\Global Settings.properties","Summary_Browser")

		strBody="<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}</style></head><body><div><div>Hello Team,</div></br><div></div>"&_
		""& strProject &" test execution is completed. Below is the quick tabular representation of execution status : </div></br><table><tr ><td >Executed by</td><td>"&_
		""& ArrHomeFolderPath(ubound(ArrHomeFolderPath))&"</td></tr><tr ><td>Environment</td><td>"&strEnvironment&"</td></tr><tr><td >Browser</td><td>"&strBrowser&"</td></tr><tr><td>Executed on </td><td>"&_
		""& Date&"</td></tr><tr><td >Total TC's #</td><td>"&intTotalTests&"</td></tr><tr><td >Passed #</td><td>"&intPassCount&"</td></tr><tr><td >Failed #</td><td>"&intFailCount&""&_
		"</td></tr><tr><td>Aborted </td><td>"&intAbortedCount&"</td></tr><tr><td>Start Time</td><td>"&strRunStartTime&_
		"</td></tr><tr><td>End Time</td><td>"&strRunEndTime&"</td></tr><tr><td>Total Duration</td><td>"&strOverAllExecutionTime&"</td></tr></table></br><p>VDIs used :"&strHostName&"</p> </br><div>Thanks,</div><div>"&_
		"SQA Automation</div></body></html>"

		call objEmailUtility.SendMail(strMailto,strMailCC,strSubject,strBody,strSummaryFilePath)


Set objfso = Nothing
Set objShell = Nothing
set objEmailUtility = Nothing
