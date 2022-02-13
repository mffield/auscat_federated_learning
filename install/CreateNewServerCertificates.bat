set server_pw=changeme
set server_alias=control

rem Create the private keys
keytool -genkeypair -alias %server_alias%_key -keyalg RSA -ext san=dns:distributedlearning.inghaminstitute.org.au -dname "CN=distributedlearning.inghaminstitute.org.au,OU=Medical Physics,O=Ingham Institute,L=Liverpool,S=NSW,C=AU" -keypass %server_pw% -keystore %server_alias%_server.jks -storepass %server_pw%
keytool -genkeypair -alias %server_alias%_webpagekey -keyalg RSA -dname "CN=webclient,OU=Medical Physics,O=Ingham Institute,L=Liverpool,S=NSW,C=AU" -keypass %server_pw% -keystore %server_alias%_webpage.jks -storepass %server_pw%

rem  Create the p12 files - these can be imported into internet browsers
keytool -importkeystore -srckeystore %server_alias%_webpage.jks -destkeystore %server_alias%_webpage.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %server_pw% -storepass %server_pw% -keypass %server_pw% -noprompt

 
rem Export certificate or public key from each keystore
keytool -exportcert -alias %server_alias%_webpagekey -file %server_alias%_webpage_public.cer -keystore %server_alias%_webpage.jks -storepass %server_pw%
keytool -exportcert -alias %server_alias%_key -file %server_alias%_public.cer -keystore %server_alias%_server.jks -storepass %server_pw%

rem Import the certificate of each client into the server trusted keystore 
keytool -importcert -keystore %server_alias%_trust.jks -alias %server_alias%_webpagekey -file %server_alias%_webpage_public.cer -storepass %server_pw% -noprompt


rem At this point we could use a self-signed certificate. Use the steps below to create a signed certificate chain


::::::::::::::::::::::::::::::::::::::::::::::::::::::
rem Create certificate signing request
keytool –keystore %server_alias%_server.jks –certreq –alias %server_alias%_key –keyalg rsa –file %server_alias%_server.csr

rem Send the output signing request to the signing authority. The result should be a signed crt file as well as root and intermediate crt.
rem The server can still be set up with self-signed certificates if this is not available
set signed_server_file=distributedlearningserver.crt

rem Display contents of keystore
keytool -list -keystore %server_alias%_server.jks -storepass %server_pw%

rem Import certificate chain into the keystore
keytool -importcert -trustcacerts -keystore %server_alias%_server.jks -alias root -file root.crt -storepass %server_pw% -noprompt
keytool -importcert -trustcacerts -keystore %server_alias%_server.jks -alias intermediate -file intermediate.crt -storepass %server_pw% -noprompt
keytool -importcert -trustcacerts -keystore %server_alias%_server.jks -alias %server_alias%_key -file %signed_server_file%.crt -storepass %server_pw% -noprompt

rem Display contents of keystore
keytool -list -keystore %server_alias%_server.jks -storepass %server_pw%

rem Export server certificate or public key from keystore
keytool -exportcert -alias %server_alias%_key -file %server_alias%_public.cer -keystore %server_alias%_server.jks -storepass %server_pw%

rem Import the signed certificate of the server into the webpage trusted keys.
keytool -importcert -keystore %server_alias%_webpage_trust.jks -alias %server_alias%_key -file %server_alias%_public.cer -storepass %server_pw% -noprompt