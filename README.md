# Monopoly Deal

## Introduction

I originally started making this project with the intention of it being something that I just play with my friends and myself. We had been wanting to be able to play Monopoly Deal remotely for the better part of a decade now, so I made it since there wasn't any solution that appeased us. There seems to be a niche community that would also like to see this, so here it is. I've made it barely functional enough to release it.

## Massive Disclaimers

This project is far from polished or even feature complete. All cards from the base game have been implemented **except** houses/hotels. They're low priority as they are usually as good as money in our house rules(extra money cards are added to the deck to compensate). Speaking of house rules, this game uses my house rules which will almost certainly differ from how you play. Rule customization is something I very much want to implement in the future. You'll definitely encounter quite a few UI bugs, especially if you scale the the UI up/down. If there's a UI bug that prevents you from taking action, restarting your client will likely fix it. There's many ways the server can crash if something goes slightly wrong(especially when misusing commands), so be cautious if you value the game you're currently playing.

## How To Use

### Prerequisites

[Java 8](https://www.java.com/en/download/manual.jsp) is required to run both the client and the server. Later versions of Java may or may not work.

Operating the server will require a little bit of technical knowhow. If you are hosting the server, you will need to port-forward or use a proxy like [Ngrok](https://ngrok.com/). The default port is 27599, this may be changed in the config.txt file that is generated by the server.

### Server Setup

1. [Download the latest server jar file](https://github.com/OldManAlpha/Monopoly-Deal/releases) and put it in its own folder(it will generate files inside the same folder it's in).
2. Open the command line and navigate to the folder where the server jar is([or create an batch file to automate the process](https://www.windowscentral.com/how-create-and-run-batch-file-windows-10)).
3. Run `java -jar MDServer.jar`
4. The server will generate a few files on its first run, including the config.txt, which you can edit to change the server port.
5. Add users in the console by typing `registerplayer [Player Name]`. A randomly generated ID will be given and that is what the user will use to login with. View all your registered players with `listregisteredplayers`.
6. After all the desired players have connected, you start the game with the `start` command.
7. When the game finishes or you just want to restart, you can use the `reset` command to put all the cards back in the deck.

Additional undocumented commands may be seen by using the `help` command.

### Client Setup

1. [Download the latest client jar file](https://github.com/OldManAlpha/Monopoly-Deal/releases) and put it in its own folder(it will generate a file inside the same folder it's in).
2. Open the command line and navigate to the folder where the client jar is([or create an batch file to automate the process](https://www.windowscentral.com/how-create-and-run-batch-file-windows-10)).
3. Run `java -jar MDClient.jar`
4. Connect to the host's server with [their IPv4 address](https://whatismyipaddress.com/) and forwarded port(Example: `192.168.0.1:27599` or if you're the host, `localhost:27599`).
5. Input the User ID the host has assigned to you.
6. Connect and play.

## Modding Support

If you're a developer, you can create mods for the server. A guide for modding will be made Soon™, but the gist of it is you create a class that extends `MDMod` and override `onLoad()` as an entry point for your code. You can create custom decks, custom cards, listen to events, etc. Export your jar and put it in the mods folder of the server. Modding is subject to massive changes and improvements as it's just something I whipped together.

There's a ton of custom cards I made that I'll probably release as a mod.

## <img src="https://discord.com/assets/2c21aeda16de354ba5334551a883b481.png" width="80" height="80"> Discord

Come join [the Discord](https://discord.gg/9dKvSguVM4) if you'd like to chat about Monopoly Deal, make suggestions, or look for a game.

<br><br><br><br>***This project is not affiliated with Hasbro in any way.***
