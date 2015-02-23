# A sample Java REST service using Jersey and Maven and MongoDB

The project uses Java, Maven, Mongotemplate and a JAX-RS library called Jersey to create a very simple REST API app. The project is executable and has been verified in Linux env and could be deployed and run in Windows OS as well since Java(JVM) is platform independent language.

The app has four REST API end points:

    HTTP POST to authenticate a user based on a login/password passed in a JSON input payload and verify the user crendential info against the "user" and "ServiceAuth" collections(tables) of "userdb" MongoDB.

        http://<IP address>:8080/restapi/v1/login
        

    HTTP GET to return all user result set from the "user" collection(table) of "userdb" Mongodb in a JSON output payload and the result set should be filtered by a URL parameter (can be city, profession, etc) and grouped by parameter.

        http://<IP address>:8080/restapi/v1/allusers/field/{field}/value/{value}/group/{group}
        

    HTTP GET to check and return the status of all the dependant components in a JSON output payload. In the project, it is Mongodb.
    
        http://<IP address>:8080/restapi/v1/componentsstatus        
        
    
    HTTP GET to return the list of files in a given directory by a query parameter in a JSON output payload.
    
        http://<IP address>:8080/restapi/v1/filelist?directory={directory}
        
        

This project also shows how to implement Authentication & Authorization with Custom HTTP Header. Generally, there are only a few important items that makes up the authenticator and that that is: service key, authorization token, username and password. The username and password will commonly go in pairs in a json playload.

###Service Key
In some public REST API service, a service key and sometimes known as API key, is generated by the system and then sends to the user/client (either through email or other means) that is permitted to access the REST service. So besides login into the REST service with just mere username and password, the system will also check on the service key if the user/client is permitted to access the REST APIs. The usernames, passwords and service keys are all predefined in the codes above for now only demo purpose.

###Authorization Token
Upon authentication (through the login() method), the system will then generate an authorization token for the authenticated user. This token is passed back to the user/client through HTTP response and is to be used for any REST API invocation later. The user/client will have to find a way to store and use it throughout the login session. We�ll look at that in the implementation.

###Required HTTP Headers Name Definition

 
        HTTP Header Name   Description
        ========================================================
        service_key        The service key that enables a HTTP client to access the REST Web Services. This is the first layer of authenticating and authorizing the HTTP Request. 
        auth_token         The token generated upon username/password authentication, which is to be used for any REST Web Service calls (except for the authentication method shown later). 

Note: "service_key to username" mapping table is defined as the 'serviceAuth' collections of Mongodb. "auth_token to user" mapping table is defined as the hash map in side the Java 'Authenticator' Object. Because the "auth_token" is the running time data. 
      Both mapping could be enhaned with the expiration mechanism in the product reality.

# Technologies Used

        1. Jersey 2.5.1
        2. JDK 1.6
        3. Tomcat 7
        4. Maven 3.x
        5. spring-data-mongodb 1.5.0
        6. Spring Tool Suite IDE

###Why Jersey?
Java defines REST support via the Java Specification Request (JSR) 311. This specification is called JAX-RS (The Java API for RESTful Web Services). JAX-RS uses annotations to define the REST relevance of Java classes.
Jersey is the reference implementation for the JSR 311 specification.
The Jersey implementation provides a library to implement Restful webservices in a Java servlet container.
On the server side Jersey provides a servlet implementation which scans predefined classes to identify RESTful resources. In your web.xml configuration file your register this servlet for your web application.
The Jersey implementation also provides a client library to communicate with a RESTful webservice.
The base URL of this servlet is the end point like: 

       http://your_domain:port/display-name/url-pattern/path_from_rest_class

This servlet analyzes the incoming HTTP request and selects the correct class and method to respond to this request. This selection is based on annotations in the class and methods.
A REST web application consists, therefore, out of data classes (resources) and services. These two types are typically maintained in different packages as the Jersey servlet will be instructed via the web.xml to scan certain packages for data classes.
JAX-RS supports the creation of XML and JSON via the Java Architecture for XML Binding (JAXB).       

JAX-RS annotations
The most important annotations in JAX-RS are listed in the following table

        Annotation                      Description          
        ========================================================
        @PATH(your_path)                                Sets the path to base URL + /your_path. The base URL is based on your application name, the servlet and the URL pattern from the web.xml configuration file.
        @POST                                           Indicates that the following method will answer to an HTTP POST request.
        @GET                                            Indicates that the following method will answer to an HTTP GET request.
        @PUT                                            Indicates that the following method will answer to an HTTP PUT request.
        @DELETE                                         Indicates that the following method will answer to an HTTP DELETE request.
        @Produces(MediaType.TEXT_PLAIN[, more-types])   @Produces defines which MIME type is delivered by a method annotated with @GET. In the example text ("text/plain") is produced. Other examples would be "application/xml" or "application/json".
        @Consumes(type[, more-types])                   @Consumes defines which MIME type is consumed by this method.
        @PathParam                                      Used to inject values from the URL into a method parameter. This way you inject, for example, the ID of a resource into the method to get the correct object.         

###Why MongoDB?
Best Practices for MongoDB
NoSQL products (and among them MongoDB) should be used to meet challenges. If you have one of the following challenges, you should consider MongoDB:

You Expect a High Write Load
MongoDB by default prefers high insert rate over transaction safety. If you need to load tons of data lines with a low business value for each one, MongoDB should fit. Don't do that with $1M transactions recording or at least in these cases do it with an extra safety.

You need High Availability in an Unreliable Environment (Cloud and Real Life)
Setting replicaSet (set of servers that act as Master-Slaves) is easy and fast. Moreover, recovery from a node (or a data center) failure is instant, safe and automatic

You need to Grow Big (and Shard Your Data)
Databases scaling is hard (a single MySQL table performance will degrade when crossing the 5-10GB per table). If you need to partition and shard your database, MongoDB has a built in easy solution for that.

Your Data is Location Based
MongoDB has built in spacial functions, so finding relevant data from specific locations is fast and accurate.

Your Data Set is Going to be Big (starting from 1GB) and Schema is Not Stable
Adding new columns to RDBMS can lock the entire database in some database, or create a major load and performance degradation in other. Usually it happens when table size is larger than 1GB (and can be major pain for a system like BillRun that is described bellow and has several TB in a single table). As MongoDB is schema-less, adding a new field, does not effect old rows (or documents) and will be instant. Other plus is that you do not need a DBA to modify your schema when application changes.

###Why Tomcat?
Tomcat is used for this project as the web application server to host the REST API service. This project could also be deployed to the other Java platform like Oracle Weblogic, JBoss, Jetty.
Apache Tomcat is an open source application server that (in version 7.0) implements the Servlet 3.0 and JavaServer Pages 2.2 specifications, and includes many additional features that make it a useful and simple platform for developing and deploying web applications and web services.

###Why spring-data-mongodb?
The Spring Data MongoDB project provides integration with the MongoDB document database. Key functional areas of Spring Data MongoDB are a POJO centric model for interacting with a MongoDB DBCollection and easily writing a Repository style data access layer.

# Deployment instructions

###Install MongoDB
1. By following the instructions on http://docs.mongodb.org/manual/tutorial/install-mongodb-on-red-hat-centos-or-fedora-linux/ , run

        sudo yum install -y mongodb-org
        sudo /sbin/service mongod restart

2. Run Command to check the staut of MongoDB to make sure the Mongod service running
        
        /sbin/service mongod status
        
3. Rum command to initalize the 'user' collection of 'userdb' database with user.json from this project
        
        mongoimport -d userdb -c user --file ./user.json --upsert

4. Rum command to initalize the 'serviceAuth' collection of 'userdb' database with serviceAuth.json from this project
        
        mongoimport -d userdb -c serviceAuth --file ./serviceAuth.json --upsert

###Install Tomcat 7
1. Download Tomcat 7 in /user/local

        wget http://mirror.nexcess.net/apache/tomcat/tomcat-7/v7.0.59/bin/apache-tomcat-7.0.59.tar.gz
        tar xvzf pache-tomcat-7.0.59.tar.gz
       
2. Tomcat 7 could be configured as the Linux service or run as the daemon with the command

        /user/local/apache-tomcat-7.0.59/bin/startup.sh

###Install maven
1. Download Maven in /user/local

        wget http://mirror.olnevhost.net/pub/apache/maven/binaries/apache-maven-3.2.1-bin.tar.gz
        tar xvf apache-maven-3.2.1-bin.tar.gz
        
2. Setup Maven

        export M2_HOME=/usr/local/apache-maven/apache-maven-3.2.1
        export M2=$M2_HOME/bin
        export PATH=$M2:$PATH 

###Build the package of the app
1. Download the project to /home/<user>
2. Modify the MongoDB host of Mongo bean in RestAPI/src/main/resource/applicationContext.html

        <bean id="mongo" class="org.springframework.data.mongodb.core.MongoFactoryBean">
            <property name="host" value="192.168.1.70"/>
        </bean>
        
3. Run command to the package

        cd RestAPI
        mvn clean
        mvn clean package

###Deploy the package to Tomcat 7       
1. Shut down Tomcat 

        /user/local/apache-tomcat-7.0.59/bin/shutdown.sh
        
2. Install the above app packate in Tomcat by using the followng command. Of course, we could install in through the Tomcat console http://<ip address>:8080

        cp /home/<user>/RestAPI/target/restapi.war /usr/local/pache-tomcat-7.0.59/webapps

3. Start Tomcat

        /user/local/apache-tomcat-7.0.59/bin/startup.sh
        
4. Do smoke test of the rest api by the command to get "Hey, This is REST API" back

        wget http://<ip address>:8080/restapi/v1
        Hey, This is REST API 
        


# Test the App
We could use "RestClient" ADD-ON of Firefox internet browser to do the tests. To install RestClient ADD-ON in FireFox internet Browse , please follow the instrunctions from https://addons.mozilla.org/en-us/firefox/addon/restclient/

###Test end point: HTTP POST to authenticate a user based on a login/password passed in a JSON input payload and verify the user crendential info against the "user" and "ServiceAuth" collections(tables) of "userdb" MongoDB.
"
Steps:

1. Input 'http://<IP address>:8080/restapi/v1/login' in "URL" input field

2. Select "POST" methd

3. Add 'Content-type' Request Header with the 'application/json' value.

4. Add 'service_key' Request Header with the '5fc5f8c3-1a60-42f6-8147-f4e86b1ce952' value.

5. Add '{"name":"User1","password":"Password1"}' as the Request Body

6. Click on 'SEND' Button

Response:

The auth_token will be return

        'cc2ea196-72fb-4b06-8cc3-fab5cefb21e3' 
 
###Test end point: HTTP GET to return all user result set from the "user" collection(table) of "userdb" Mongodb in a JSON output payload and the result set should be filtered by a URL parameter (can be city, profession, etc) and grouped by parameter.
Steps:

1. Input 'http://<IP address>:8080/restapi/v1/allusers/field/profession/value/Engineer/group/city' in "URL" input field

2. Select "GET" methd

3. Add 'auth_token' Request Header with the above token from the response.

4. Add 'service_key' Request Header with the '5fc5f8c3-1a60-42f6-8147-f4e86b1ce952' value.

5. Click on 'SEND' Button

Response:

The JSON playload will be return

        [{"personId":"3","name":"User3","password":"Password3","city":"city1","age":42,"profession":"Engineer"},{"personId":"7","name":"User7","password":"Password7","city":"city1","age":36,"profession":"Engineer"},{"personId":"9","name":"User9","password":"Password9","city":"city1","age":36,"profession":"Engineer"},{"personId":"11","name":"User11","password":"Password11","city":"city1","age":26,"profession":"Engineer"},{"personId":"5","name":"User5","password":"Password5","city":"city3","age":46,"profession":"Engineer"}]

###Test end point: HTTP GET to check and return the status of all the dependant components in a JSON output payload. In the project, it is Mongodb.
Steps:

1. Input 'http://<IP address>:8080/restapi/v1/filelist?directory=/tmp' in "URL" input field

2. Select "GET" methd

3. Add 'auth_token' Request Header with the above token from the response.

4. Add 'service_key' Request Header with the '5fc5f8c3-1a60-42f6-8147-f4e86b1ce952' value.

5. Click on 'SEND' Button

Response:

The JSON playload will be return

        ["temp file1","temp file2"]

###Test end point: HTTP GET to return the list of files in a given directory in a JSON output payload.
Steps:

1. Input 'http://<IP address>:8080/restapi/v1/componentsstatus' in "URL" input field

2. Select "GET" methd

3. Add 'auth_token' Request Header with the above token from the response.

4. Add 'service_key' Request Header with the '5fc5f8c3-1a60-42f6-8147-f4e86b1ce952' value.

5. Click on 'SEND' Button

Response:

The JSON playload will be return

        [{"component":"Mongo","status":"mongod (pid 6477) is running..."}]

# How to version the service
The answer was generally pretty simple: if the contract is the service, and the service is exposed as a URL, then the solution is to version the URL and map the different URL to a different version app as a different serlvet-mapping in web.xml. As such, the endpoints are
        
        http://<IP address>:8080/restapi/v1
        http://<IP address>:8080/restapi/v1/login
        http://<IP address>:8080/restapi/v1/allusers/field/{field}/value/{value}/group/{group}
        http://<IP address>:8080/restapi/v1/filelist?directory={directory}
        http://<IP address>:8080/restapi/v1/componentsstatus

# How to implement the pagination?
The solution should be to add two more '@PathParam' URL paramenters into the end point URL in order to pass in the page size and the page number and the URL of the endpoint will look like
        
        http://<IP address>:8080/restapi/v1/allusers/field/{field}/value/{value}/group/{group}/pagesize/{pagesize}/pagenumber/{pagenumber}

The service could translate the values to the MongoTemplate Query object as the parameter of 'skip' method and the parameter of 'limit' method. 
 
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value)); 
        query.with(new Sort(Sort.Direction.ASC,group));
        query.skip(pageSize * (pageNumber -1 ));
        query.limit(pageSize);
