set server_pw=changeme
set IPAddress=0.0.0.0


rem Create the private keys
keytool -genkeypair -alias serverkey -keyalg RSA -dname "CN=%IPAddress%,OU=Medical Physics,O=Ingham Institute,L=Liverpool,S=NSW,C=AU" -keypass %server_pw% -keystore ozcat_server.jks -storepass %server_pw%
keytool -genkeypair -alias webpagekey -keyalg RSA -dname "CN=webclient,OU=Medical Physics,O=Ingham Institute,L=Liverpool,S=NSW,C=AU" -keypass %server_pw% -keystore ozcat_webpage.jks -storepass %server_pw%
keytool -genkeypair -alias liverpool_key -keyalg RSA -dname "CN=liverpool,OU=Liverpool and Macarthur Cancer Therapy Centre,O=Liverpool Hospital,L=Liverpool,S=NSW,C=AU" -keypass %server_pw% -storepass %server_pw% -keystore liverpool_key.jks
keytool -genkeypair -alias westmead_key -keyalg RSA -dname "CN=westmead,OU=Crown Princess Mary Cancer Therapy Centre,O=Westmead Hospital,L=Westmead,S=NSW,C=AU" -keypass %server_pw% -storepass %server_pw% -keystore westmead_key.jks
keytool -genkeypair -alias wollongong_key -keyalg RSA -dname "CN=wollongong,OU=Illawarra Cancer Care Centre,O=Wollongong Hospital,L=Wollongong,S=NSW,C=AU" -keypass %server_pw% -storepass %server_pw% -keystore wollongong_key.jks

rem  Create the p12 files - these can be imported into internet browsers
keytool -importkeystore -srckeystore ozcat_server.jks -destkeystore ozcat_server.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %server_pw% -storepass %server_pw% -keypass %server_pw% -noprompt
keytool -importkeystore -srckeystore ozcat_webpage.jks -destkeystore ozcat_webpage.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %server_pw% -storepass %server_pw% -keypass %server_pw% -noprompt
keytool -importkeystore -srckeystore liverpool_key.jks -destkeystore liverpool_key.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %server_pw% -storepass %server_pw% -keypass %server_pw% -noprompt
keytool -importkeystore -srckeystore westmead_key.jks -destkeystore westmead_key.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %server_pw% -storepass %server_pw% -keypass %server_pw% -noprompt
keytool -importkeystore -srckeystore wollongong_key.jks -destkeystore wollongong_key.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass %server_pw% -storepass %server_pw% -keypass %server_pw% -noprompt
 
rem Export certificate or public key from each keystore
keytool -exportcert -alias webpagekey -file ozcat-webpage-public.cer -keystore ozcat_webpage.jks -storepass %server_pw%
keytool -exportcert -alias liverpool_key -file liverpool-public.cer -keystore liverpool_key.jks -storepass %server_pw%
keytool -exportcert -alias westmead_key -file westmead-public.cer -keystore westmead_key.jks -storepass %server_pw%
keytool -exportcert -alias wollongong_key -file wollongong-public.cer -keystore wollongong_key.jks -storepass %server_pw%

rem Import the certificate of each client into the server trusted keystore 
keytool -importcert -keystore ozcat_server_trust.jks -alias webpagekey -file ozcat-webpage-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore ozcat_server_trust.jks -alias liverpool_key -file liverpool-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore ozcat_server_trust.jks -alias westmead_key -file westmead-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore ozcat_server_trust.jks -alias wollongong_key -file wollongong-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore ozcat_server_trust.jks -alias serverkey -file ozcat-server-public.cer -storepass %server_pw% -noprompt

rem Export public key from server keystore
keytool -exportcert -alias serverkey -file ozcat-server-public.cer -keystore ozcat_server.jks -storepass %server_pw%


rem Import the certificate of server into each client trusted keystore 
keytool -importcert -keystore ozcat_webpage_trust.jks -alias servercert -file ozcat-server-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore liverpool_trust.jks -alias servercert -file ozcat-server-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore westmead_trust.jks -alias servercert -file ozcat-server-public.cer -storepass %server_pw% -noprompt
keytool -importcert -keystore wollongong_trust.jks -alias servercert -file ozcat-server-public.cer -storepass %server_pw% -noprompt


rem view the contents of the client keystores 
keytool -list -keystore liverpool_trust.jks -storepass %server_pw%
