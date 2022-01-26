vi jpsall

#!/bin/bash
    
for host in k8s-node1 k8s-node2
do
 echo =============== $host ===============
 ssh $host jps 
done
    
    
    
    chmod +x jpsall