# COOP Demo 2 & 3 — Picsum Image Browser + Offline Cache (Room)

This project demonstrates API networking, state management, image loading, and persistent offline storage in Android using **Kotlin**, **Jetpack Compose**, **Retrofit**, **Coil**, and **Room**.  
It was developed as part of the Red River College COOP Android Demo series.

---

##  Demo 2 — Picsum Image Browser (API + ViewModel + Compose)

**Goal:** Fetch and display images from the Picsum REST API using modern Android tools.

### Features
- Fetch a random image from the Picsum API  
- Optional author name filter  
- Loading indicator while fetching  
- Error handling for failed network calls  
- Display image + author using **Coil**  
- “Open Source” button using an **Android Intent**  
- ViewModel + StateFlow used for state management  

###  Tech Used
- **Jetpack Compose** – modern UI toolkit  
- **Retrofit** – network/API calls  
- **Moshi** – JSON parsing  
- **Coil** – image loading  
- **ViewModel** – lifecycle-safe state  
- **StateFlow** – reactive UI updates  

---

##  Demo 3 — Offline Image Cache (Room Database)

**Goal:** Extend Demo 2 by saving the last successfully loaded image to a local Room database.

###  Features Added
- Save the last fetched image (URL + author) into Room  
- Load and display the saved image when offline  
- UI button: **Load Last Saved Image (Room)**  
- Works even if the API is unreachable  
- Demonstrates Repository pattern with local + remote data sources  

###  New Tech Used
- **Room Database**  
- **DAO (Data Access Object)**  
- **Entity + Table schema**  
- **Repository pattern**  
- **Suspend functions with Coroutines**

---

## Project Structure

