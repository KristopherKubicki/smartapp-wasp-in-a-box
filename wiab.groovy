/**
 *  Wasp in a Box - This is a simple algorithm to create an "occupancy" concept when
 *    paired with simulated presence sensors.  This implementation was borrowed from 
 *  http://dreamgreenhouse.com/projects/2013/presence/index.php
 *
 * Version 0.1: Initial commit
 * Version 0.2: Adds support for motion perimeter sensors
 *
 */
 
definition(
    name: "Wasp in a Box",
    namespace: "KristopherKubicki",
    author: "kristopher@acm.org",
    description: "Implements the 'Wasp in a Box' algorithm for simulated presence sensors",
    category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	section("Control this Occupancy Sensor..."){
		input "occupy", "capability.presenceSensor", title: "Which?"
	}
	section("When there's movement..."){
		input "imotions", "capability.motionSensor", title: "Where?", multiple: true, required:false
        input "icontacts", "capability.contactSensor", title: "Where?", multiple: true, required:false
	}
    section("And then off when any of these perimeter contact sensors are opened or tripped..."){
		input "ocontacts", "capability.contactSensor", title: "Where?", multiple: true, required: false
        input "omotions", "capability.motionSensor", title: "Where?", multiple: true, required:false
	}
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	unschedule()
	initialize()
}

def initialize() {
	subscribe(icontacts, "contact.open", insideHandler)
	subscribe(imotions, "motion.active", insideHandler)
    subscribe(ocontacts, "contact.open", outsideHandler)
    subscribe(omotions, "motion.active", outsideHandler)
}


def insideHandler(evt) {
	log.debug "$evt.name: $evt.value"
    def success = 1
    for (sensor in settings.ocontacts) { 
       if(sensor.latestValue("contact") == "open") { 
           success = 0
       }
    }
    for (sensor in settings.omotions) { 
       if(sensor.latestValue("motion") == "active") { 
           success = 0
       }
    }
    if(success == 1) { 
        occupy.arrived()
    }
}

def outsideHandler(evt) {
	log.debug "$evt.name: $evt.value"
    occupy.departed()
}

