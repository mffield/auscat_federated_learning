% This function concatenates parameters into a message from a list of parameter names.
function [message]=ConstructMessage(parameter_names,param)

message=[];
for j=1:length(parameter_names)
    message=strcat(message,parameter_names{j});
    message=strcat(message,';');
    values = param.(parameter_names{j})(:)';
    for k = 1:length(values)
        if mod(values(k),1)==0
            message=strcat(message,num2str(values(k),'%i;'));
        else
            message=strcat(message,num2str(values(k),'%f;'));
        end
    end
%     message=strcat(message,num2str(param.(parameter_names{j})(:)','%f;'));
    if message(end)~=';'
        message = [message ';'];
    end
end

% message = message(1:end-1);

end