set password=changeme
set clientName=liverpool
set organisationUnit=Liverpool and Macarthur Cancer Therapy Centre
set organisation=Liverpool Hospital
set location=Liverpool
set state=NSW
set country=AU

rem Create the private key ------ Change the details below
keytool -genkeypair -alias %clientName%_key -keyalg RSA -dname "CN=%clientName%,OU=%organisationUnit%,O=%organisation%,L=%location%,S=%state%,C=%country%" -keypass %password% -storepass %password% -keystore %clientName%_key.jks

rem  Create the p12 files - these can be imported into internet browsers to allow connection to web client page
keytool -importkeystore -srckeystore %clientName%_key.jks -destkeystore %clientName%_key.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %password% -storepass %password% -keypass %password% -noprompt
 
rem Export certificate or public key from keystore
keytool -exportcert -alias %clientName%_key -file %clientName%-public.cer -keystore %clientName%_key.jks -storepass %password%

rem Import the certificate of each client into the server trusted keystore -----> to be run on server side
rem keytool -importcert -keystore ozcat_server_trust.jks -alias %clientName%_key -file %clientName%-public.cer -storepass ***** -noprompt

rem Import the certificate of server into each client trusted keystore 
keytool -importcert -keystore %clientName%_trust.jks -alias servercert -file ozcat-server-public.cer -storepass %password% -noprompt

rem view the contents of the client keystores 
keytool -list -keystore %clientName%_trust.jks -storepass %password%
