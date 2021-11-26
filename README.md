# DSSTimeShifter
A script/program to shift dss timeseries data forward to handle a specific issue in the Trinity WAT Project

This project takes in data from the DCInvestigator WAT Plugin in the form of an XML file of failed events. Then for each of those failed events, adjusts the dss data in that 
event's timewindow a set number of days forward, deleting the data at the end of the year to make room for the now filler zeroes in the beginnning.
