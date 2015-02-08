# SmartThings "Wasp in a Box"

A common request for SmartThings is the ability to create context-aware occupancy sensors.  SmartThings does not have this capability currently, but it can be added with some basic tools. 

The first thing you need to create a context-aware occupancy sensor is a "Simulated Presence Sensor" in the SmartThings IDE. Once you've created this device, you'll need this SmartApp in order to manage it automatically. 

# Implementation 

How does it work?  Rob Collingridge created a very simple algorithm on Vera that creates a box bounded by doors.  Whenever a door is opened, the occupancy sensor exists in an unsure or "away" state.  Whenever the door is closed and motion is detected inside the box (a.k.a, "the wasp"), the occupancy sensor becomes "present". 

Please feel free to read more about Rob's implementation here:  http://dreamgreenhouse.com/projects/2013/presence/index.php

This implementation goes a step further by allowing a box to be bound by motion sensors as well as door sensors, or even a combination of them.

# Tips

I use this SmartApp to manage nested zones.  I have a sensor for the whole house, one for upstairs, one for the bedroom, and another for the bathroom accessable only from the bedroom. 

# Bugs

Although this algorithm is relatively simple to understand, it comes with many caveats.  
* Delays between the states of the various sensors can cause problems.  
* SmartThings does not allow the SmartApp to specify Simulated Presence Sensors -- just Presence Sensors.  Thus, you can currently select a presence sensor that may not have a command implemented to allow present() or away(). 

# Roadmap

Eventually I am going to create an interface for nested occupancy, so the algorithm can be even more aware of people moving through the zones.
