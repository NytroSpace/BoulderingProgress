The application has been made for the recording of bouldering progress. 


# Route (Record):
- grade (String)
- wallType (WallType enum)
- attempts (int)
- isCompleted (boolean)
- UUID (String)

Route is of record type and groups together all the relevant route information.

### WallType (Enum)
- SLAB
- VERTICAL
- OVERHANG
- ROOF

WallType is an enum class used inside a Route to determine the type of wall.

---


# Session (CLass)
- listOfRoutes (Route)
- date (String)
- UUID (String)

Session is a class that holds session specific information, as well as a list of all the routes done in that session.

---

# MonthlyDataset (Class)
- listOfSession (Session)
- month (Integer)
- year (Integer)

Same as the session class but one layer higher, so it holds month relevant data and a list of sessions in that month.

--- 

# MonthlySummary (Record)
- month (String)
- year (Integer)
- path (String)

MonthlySummary is used so that MainController doesn't get bloated with unnecessary data. Basically there is no reason
for MainController to hold all the data for old months. So instead, we have a list of summaries which contain a path to 
the file. If a user clicks on an old month, we use the path to pull the relevant data from that month.

---

# JsonDataService (Class)

This class deals with saving data as a JSON file and pulling out data from the JSON file and then converting it into
objects using the ObjectMapper
 
