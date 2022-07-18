pushd %~dp0
java -cp ".;.\supportlibraries\External_Jars\*" -DRunConfiguration=%~1 -DManualTCID=%~2 -DBrowser=%~3 allocator.AzureRunner