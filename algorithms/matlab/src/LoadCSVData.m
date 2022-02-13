function [data] = LoadCSVData(config,features,target)

% File is expected to contain:
% Comma separated variables - at least columns survival time and vital status

DataTable = readtable(config.dataLocation);
columnHeaders = DataTable.Properties.VariableNames;
Data = table2cell(DataTable);


for ii=1:size(columnHeaders,2); col_ind.(columnHeaders{ii}) = ii; end

Data(:,col_ind.vital)=num2cell(~cell2mat(Data(:,col_ind.vital)));

features = strsplit(features{1},',');

dose_threshold = 47.9; % difference between curative and palliative treatments
number_of_patients = size(Data,1);
survival_threshold = 24;
if all(ismember(features,columnHeaders))
    feature_num=zeros(length(features),1);
    for k=1:length(features) 
        feature_num(k) = col_ind.(features{k});
    end
    x = Data(:,feature_num); % Retrieve data points
    for k=1:length(features) 
        if ischar(x{1,k})
            x(:,k) = cellfun(@(x) str2double(x),x(:,k),'UniformOutput',false);
        end
    end
    x = cell2mat(x);
    y = double(cell2mat(Data(:,col_ind.(target)))>survival_threshold); % Retrieve prediction target

    y(y==0)=-1; % y(y==-1)=0;
    y=y';
    x=x';
else
    disp('Error: feature included in model not present in data set');
    x=[]; y=[];
end

surv_time = (Data(:,col_ind.survival_time));
vital = (Data(:,col_ind.vital));   
data.x = x; data.y=y; data.surv_time = surv_time; data.vital=vital; 

n = ~any(isnan(x));
data.x = x(:,n); data.y=y(n); data.surv_time = surv_time(n); data.vital=vital(n); 


end
