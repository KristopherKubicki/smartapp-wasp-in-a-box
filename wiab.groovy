/**
 *  Wasp in a Box - This is a simple algorithm to create an "occupancy" concept when
 *    paired with simulated presence sensors.  This implementation was borrowed from 
 *  http://dreamgreenhouse.com/projects/2013/presence/index.php
 *
 *
 */
 
definition(
    name: "Wasp in a Box",
    namespace: "KristopherKubicki",
    author: "Kristopher Kubicki",
    description: "Implements the 'Wasp in a Box' algorithm for simulated presence sensors",
    category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	section("Control this Occupancy Sensor..."){
		input "occupy", "capability.presenceSensor", title: "Which?"
	}
	section("When there's movement..."){
		input "motions", "capability.motionSensor", title: "Where?", multiple: true
	}
    section("And then off when any of these perimeter contact sensors are opened..."){
		input "contacts", "capability.contactSensor", title: "Where?", multiple: true
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
	subscribe(motions, "motion.active", motionHandler)
    subscribe(contacts, "contact.open", contactHandler)
}


def motionHandler(evt) {
	log.debug "$evt.name: $evt.value"
    def success = 1
    for (sensor in settings.contacts) { 
       if(sensor.latestValue("contact") == "open") { 
           success = 0
       }
    }
    if(success == 1) { 
        occupy.arrived()
    }
}

def contactHandler(evt) {
	log.debug "$evt.name: $evt.value"
    occupy.departed()
}
