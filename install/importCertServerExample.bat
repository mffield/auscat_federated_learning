set password=changeme
set server_alias=ozcat

set clientName=liverpool
keytool -importcert -keystore %server_alias%_trust.jks -alias %clientName%_key -file %clientName%-public.cer -storepass %password% -noprompt

set clientName=wollongong
keytool -importcert -keystore %server_alias%_trust.jks -alias %clientName%_key -file %clientName%-public.cer -storepass %password% -noprompt

set clientName=westmead
keytool -importcert -keystore %server_alias%_trust.jks -alias %clientName%_key -file %clientName%-public.cer -storepass %password% -noprompt


rem view the contents of the client keystores 
keytool -list -keystore %server_alias%_trust.jks -storepass %password%