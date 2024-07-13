# Server Setup Instructions

## Prerequisites

1. **Install Java 8:**
   - Download Java 8 from [Oracle's website](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html).
   - Verify the installation by running `java -version` in your command prompt or terminal. It should display Java 8 details.

2. **Download Ngrok:**
   - Sign up for a free Ngrok account at [ngrok.com](https://ngrok.com/).
   - Download Ngrok and follow the installation instructions provided on the website.

## Running the Server

1. **Download the Server Jar:**
   - Obtain the latest server jar file from the game's official source.

2. **Start the Server:**
   - Open a command prompt or terminal.
   - Navigate to the directory where the server jar file is located:
     ```sh
     cd path/to/server-jar-directory
     ```
   - Run the server jar using Java 8:
     ```sh
     path/to/java8/bin/java -jar server.jar
     ```
   - This will generate the necessary data files in the same folder, including `config.txt`.

3. **Configure the Server (Optional):**
   - If needed, open `config.txt` and adjust the settings (e.g., change the port if necessary).

## Exposing the Server with Ngrok

1. **Run Ngrok:**
   - Open a new command prompt or terminal.
   - Navigate to the directory where the ngrok executable is located.
   - Run Ngrok to forward the desired port:
     ```sh
     ngrok tcp 27599
     ```
   - Ngrok will display a forwarding address, for example: `tcp://0.tcp.ngrok.io:10372`.

2. **Keep the Ngrok Session Active:**
   - Leave the Ngrok command prompt or terminal window open to keep the session active.

## Connecting to the Server

1. **Share the Ngrok Address:**
   - Provide your friends with the Ngrok forwarding address: `0.tcp.ngrok.io:10372` (replace with the actual address provided by Ngrok).

2. **Friends Download and Run the Client Jar:**
   - Obtain the latest client jar file from the official source.
   - Open a command prompt or terminal.
   - Navigate to the directory where the client jar file is located:
     ```sh
     cd path/to/client-jar-directory
     ```
   - Run the client jar using Java 8:
     ```sh
     path/to/java8/bin/javaw -jar client.jar
     ```
   - Choose where to store the client’s data as prompted.

3. **Friends Connect to Your Server:**
   - In the client, enter the Ngrok forwarding address (e.g., `0.tcp.ngrok.io:10372`).

## Summary

1. **Install Java 8 and Ngrok.**
2. **Download the server jar and start the server locally on port 27599.**
3. **Run Ngrok to expose the server and get the forwarding address.**
4. **Share the Ngrok address with your friends.**
5. **Friends connect to the server using the Ngrok address in their game client.**

---

### Example Commands

**Start the Server:**
```sh
cd path/to/server-jar-directory
path/to/java8/bin/java -jar server.jar
```

**Run Ngrok:**

```sh
cd path/to/ngrok
ngrok tcp 27599
```

**Run the Client:**

```sh
cd path/to/client-jar-directory
path/to/java8/bin/javaw -jar client.jar
```
