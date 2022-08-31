# Lilian
Lilian is a java program which can help you easily get some domain's ip info

## It's realy easy to use with following steps:

### Step 1:
edit the outer ` application.yml ` file add some domain those we want to know there's ip info and split them with ` , ` .  
Here is example:  
```
url: github.com,raw.githubusercontent.com 
```

### Step 2:
just using single command starting the jar  
```
java -jar Lilian-1.0.jar --spring.config.location=path/to/your/application.yml 
```  
#### Finally we will got an ` hosts ` file with fellowing content:  
``` 
140.82.112.3  github.com  
185.199.108.133  raw.githubusercontent.com 
```
