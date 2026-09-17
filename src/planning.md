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
