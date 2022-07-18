Set wd = GetObject(, "Word.Application")
If Not IsEmpty(wd) Then
	For Each doc In wd.Documents
		If (StrComp(doc.Name, Wscript.arguments.item(0), vbTextCompare) = 0) Then
			doc.Save
			doc.Close
			wscript.quit 0
		End If
	Next

End If
wscript.quit 1
