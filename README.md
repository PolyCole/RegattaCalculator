# RegattaCalculator
Used to determine Regatta results using portsmouth yardstick handicaps.

Inspired by the regattas at YMCA Camp Warren.

Currently, text-file input is supported. The first line of the text file should 
be the wind conditions based on the Beaufort Wind Scale. If the wind is 0-3 Beaufort,
then the conditions are low. Any wind 4 and above is considered high. 

For more information on the Beaufort scale:
https://www.weather.gov/mfl/beaufort

Each subsequent line should be the entry of a boat with the name of the boat first, then
the boat's class, and finally the boat's time. Each of these should be separated by a 
colon. See hoffless.txt for an example. 

Currently there are 9 supported handicaps that are:
420 : 420s
cscow : C Scows
m16scows : M 16 Scows 
mcscows : MC Scows
xboat : X Boats
nj2k : Catalina EXPO 12.5
butterfly : Butterflies
miniscow : Miniscows
capri : Catalina Capri 14.2

More handicaps are able to be added simply by adding the name of the class and the low and
high wind handicaps to the handicaps.txt file. 




