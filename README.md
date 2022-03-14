# Distributed/Federated learning toolbox #

This toolbox contains functions for distributed machine learning across multiple data centers. A document describing the installation and usage can be found here:[AusCAT_Distributed_Machine_Learning_Framework](https://github.com/mffield/fl_auscat/blob/main/AusCAT_Distributed_Machine_Learning_Framework.pdf). The infrastructure is further described in [1] and demonstrated in pilot projects [2,3].

## MATLAB examples ##
- Sample statistics
- Logistic regression

## Python examples ##
- Sample statistics

### Installation of the following: ###

- Java JDK 1.8
- Apache Tomcat 9 
- MATLAB Compiler Runtime 2017b+ (matching version for the compiled code https://www.mathworks.com/products/compiler/matlab-runtime.html)
- Python Zeep package (https://docs.python-zeep.org/en/master/)

### Deployment: ###
To use these functions the following web applications should be loaded into your installed Apache Tomcat container: FLData.war and MPIClient.war.
A template configuration for the web application is provided in directory /install/.

## Referances ##
[1] M. Field, D. Thwaites, M. Carolan, G. Delaney, J. Lehmann, J. Sykes, S. Vinod, L. Holloway, Infrastructure platform for privacy-preserving distributed machine learning development of computer-assisted theragnostics in cancer, Journal of Biomedical Informatics (submitted).

[2] M. Field, M. Barakat, M. Bailey, M. Carolan, A. Dekker, G. Delaney, G. Goozee, L. Holloway, J. Lehmann, T. Lustberg, J. van Soest, J. Sykes, S. Walsh, D. Thwaites, A distributed data mining network infrastructure for australian radiotherapy decision support, Proc Engineering and Physical Sciences in Medicine (EPSM) 39 (1) (2015) 323. doi:https://doi.org/10.1007/s13246-015-0410-1.

[3] M. Field, S. Vinod, N. Aherne, M. Carolan, A. Dekker, G. Delaney, S. Greenham, E. Hau, J. Lehmann, J. Ludbrook, A. Miller, A. Rezo, J. Selvaraj, J. Sykes, L. Holloway, D. Thwaites, Implementation of the australian computer-assisted theragnostics (auscat) network for radiation oncology dataextraction, reporting and distributed learning, Journal of Medical Imaging and Radiation Oncology 65 (5) (2021) 627–636. doi:https://doi.org/10.1111/1754-9485.13287. URL https://onlinelibrary.wiley.com/doi/abs/10.1111/1754-9485.13287.


## Contact: ##
matthew.field at unsw.edu.au

