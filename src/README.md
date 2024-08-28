# CSC365 - Lab 7
(a) the list of all members of the team: Winton Gee
<br/><br/>

(b) any compilation/runtime instructions,
<br/>
including the names of environment variables used to pass JDBC URL, username and password
<br/> Step 1: Compile: javac InnReservations
<br/> Step 2: Run    : java InnReservations (Depends on the machine, needs to be run with mysql-connector-java-8.0.16.jar)
<br/> Step 3: Program will provide instructions on how to select the commands

(c) information about any known bugs and/or deficiencies:
<br/> Functional Requirements 3-6 are tested and contains no obvious bugs.
<br/> Functional Requirements 1-2 will fail on some edge cases, but the program will handle errors nicely.
<br/> Edge case for Functional Requirement 1: If the CheckIn date is within the period being checked for days stayed,
    but the CheckIn date is greater than 180 days, those extra days will still be included. 
