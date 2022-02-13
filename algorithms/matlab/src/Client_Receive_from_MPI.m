function [itr, model, received_parameters, central_message] = Client_Receive_from_MPI(svc,prev_itr,model)

while str2double(readMasterItr(svc)) == prev_itr
        end
        itr=int64(str2double(readMasterItr(svc)));
        central_message=readMasterMessage(svc);
        disp(strcat('Received Message from central algorithm; ',central_message))
        
        [model, received_parameters] = ParseMessage(central_message,model,model.alg_client_index);
               
end