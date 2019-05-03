# RegattaCalculator -- v2.0
Used to determine Regatta results using portsmouth yardstick handicaps.

Inspired by the regattas at YMCA Camp Warren.

RegattaCalculator v2.0 provides a more user-friendly implementation. It provides a command-line-esque
interface that the user can input commands into. The commands provide the user with the functionality for creating a regatta, generating results, outputting the 
podium of the race, and writing the results to file.

Text-file input is quite simple and hopefully inherent. The first line of the text file should 
be the wind conditions based on the Beaufort Wind Scale. If the wind is 0-3 Beaufort,
then the conditions are low. Any wind 4 and above is considered high. 

For more information on the Beaufort scale:
https://www.weather.gov/mfl/beaufort

Each subsequent line should be the entry of a boat with the name of the boat first, then
the boat's class, and finally the boat's time. Each of these should be separated by a 
colon. See hoffless.txt for an example, or use the format command in the interface. 

Currently there are 10 supported handicaps that are:
420 : 420 racing boats.
cscow : C Scows
m16scows : M 16 Scows 
mcscows : MC Scows
xboat : X Boats
nj2k : Catalina EXPO 12.5
butterfly : Butterflies
miniscow : Miniscows
capri : Catalina Capri 14.2
canoe: Canoes. 

More handicaps are able to be added simply by adding the name of the class and the low and
high wind handicaps to the handicaps.txt file, alternatively, you can add a boat to the handicaps file by using the addType command in the interface. 




