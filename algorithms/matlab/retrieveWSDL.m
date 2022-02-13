createClassFromWsdl('http://localhost/MPIClient/MPIClient?wsdl')

path = './@MPIService/';
dirstruct = dir(path);

dirstruct = dirstruct(cell2mat(cellfun(@(x) ~(strcmp('..',x) | strcmp('.',x)), {dirstruct.name},'UniformOutput',false)));
number_of_files = length(dirstruct);

for i=1:number_of_files
    
   fid = fopen([path dirstruct(i).name],'r');
   doc = cell(1); j=0;
   while ~feof(fid)
       line = fgetl(fid);
       j=j+1;
       doc(j) = {strrep(line,',''document'');',',''rpc'');')};
       
   end
   fclose(fid);
   delete([path dirstruct(i).name])
   fid = fopen([path dirstruct(i).name],'w');   
   for k=1:j
    fprintf(fid,'%s\n',doc{k});
   end
    fclose(fid);
end