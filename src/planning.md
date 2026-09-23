Planning Document

## Scenario 
There are two different tracks
- A freight line (Green and Yellow)
- Passenger line (Purple)

## There are two junctions
- The yellow freight line goes across the purple track in between places (1,2 and 5,6). Here passenger trains should always have priority 

- The passenger line splits from 2 tracks into 3. (The bottom connects to the middle, the middle creates a fork to another track (8) )

Need to simulate both different types of trains interacting and causing no collisions

# Trains can travel both ways
Trains going South
- Can enter into sections 1 & 3
- Can exit from sections 4, 8, 9 & 11

Trains going North
- Can enter into sections 4, 9, 10 & 11
- Can exit from sections 2 & 3

When you exit delete the token?
Or Epsilon Transition that takes them into another place
- But does that not make the petri net unbounded because that place can increase infinitely?

I assume there is no starting place and the petri net starts with no trains

## Transitions Needed
Left to Right (South)
Can enter into sections 1 & 3
Can exit from section 4,8,9 & 11
- 1-5, 5-8, 5-9
- 3-4
- 3-7, 7-11


Right to Left (North)
Can enter in sections 4, 9, 10 & 11
Can exit from sections 2 & 3
- 4-3
- 9-6, 6-2
- 10-6
- 11-7, 7-3

These are the places and transactions that are required


## Diagram version 1
The current diagram has the above transactions and places but it does not include 
- I will label transactions when done
- I added colors because passengers need priority and its easier to see purple needs priority

Junctions 
- Im going to color in the places that are fine to make it clearer to me

## Diagram junctions
If your in P3 or P4 you cannot cross if there is a passenger train in P1 or P6 because they have priority
- Need a way to determine this in the design

P1 and P6 mutexs determine there if there is a train respectfully in P1 and P6
If the mutex is empty that means a train is in there
If the mutex is full that means a train is not there
We can then use those mutexs to enable the freight train transitions 

## Code implementation 
Need to first implement the interlocking interface and work on those methods


Get section returns the trainName
Get trainName returns the place
- Feels like these should be switched around

Get trainName
- Accepts the trains name
- Returns the place the track is on
- Returns -1 if the train is no longer in the "rail corridor"

So the method needs to keep a list of trains that have entered the system
If there on a place we return that
If they have entered but are no longer on a place return -1
- Can keep a list of trains that entered
- Loop through the places map to see if its on a place
- If its not there return -1

There will also be the case of a train not existing, and that should return null or throw an error