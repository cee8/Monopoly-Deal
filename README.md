# Monopoly Deal

This is a recreation of the physical card game, Monopoly Deal, playable on PC platforms. Monopoly Deal contrasts from its board game counterpart in that it is quite fast-paced and can be played within 15 minutes or so. The objective is to be the first to reach 3 complete sets of properties.

I originally started making this project with the intention of it being something that I just play with my friends and myself. We had been wanting to be able to play Monopoly Deal remotely for the better part of a decade now, so I made it since there wasn't any solution that appeased us. While it is still in development and may have issues here and there, it has come a long way since its first inception. The amount of available customization and modding support can make the game exactly how you like it.

## Screenshots

<details>
  <summary>Click To View</summary>
  <img src="https://i.imgur.com/9fdPwiQ.gif">
  <img src="https://i.imgur.com/jPX6ivP.gif">
  <img src="https://i.imgur.com/Fjn5UTh.gif">
  <img src="https://i.imgur.com/JE2E5sw.gif">
  <img src="https://i.imgur.com/EJAvuAc.gif">
  <img src="https://i.imgur.com/T1wNxjA.gif">
  <img src="https://i.imgur.com/Yz8OfmJ.gif">
  <img src="https://i.imgur.com/7hRVsxS.gif">
</details>

## How To Play

### Option 1: Installer (Windows only)

[Download the latest installer](https://github.com/OldManAlpha/Monopoly-Deal/releases/download/v0.7/Monopoly-Deal-Setup-0.7.exe) and install the game.

#### Running the Game

Run the client with the shortcut on your desktop(if you chose to create one), searching for "Monopoly Deal", or navigating to C:\Program Files\Monopoly Deal(or where you chose to install it) and running `Monopoly Deal.exe`. The client's data files are stored and can be found at `C:\Users\<Username>\AppData\Roaming\Monopoly Deal`.

You can play Singleplayer straight away, or connect to a server. To connect to a server, type in the IP:Port provided by the server host. If you're playing on a server hosted on your own computer, connect to `localhost:27599`.

#### Hosting the Server

Navigate to C:\Program Files\Monopoly Deal(or where you chose to install it) and run `Start Server.bat`. From there, you can play by yourself with bots, or you may invite your friends to play by giving them your IP and port used for the server(example: `123.456.7.89:27599`). However, to be able to have your friends join, you will need to port-forward or use a proxy like [Ngrok](https://ngrok.com/). The default port is 27599, this may be changed in the config.txt file that is generated by the server. The server files can be found at `C:\Users\<Username>\AppData\Roaming\Monopoly Deal\server`.

### Option 2: Jar Files (All Operating Systems)

<details>
  <summary>Expand Instructions</summary>
  
[Java 8](https://java.com/en/download/) is required to run both the client and the server. Later versions of Java probably will not work.

#### Running the Game

[Download the latest client jar](https://github.com/OldManAlpha/Monopoly-Deal/releases) and run it with javaw.exe from your Java 8 installation. You will be asked if you want to store the client's data in the application data folder on your PC or in the same folder the jar is running in.

You can play Singleplayer straight away, or connect to a server. To connect to a server, type in the IP:Port provided by the server host. If you're playing on a server hosted on your own computer, connect to `localhost:27599`.

#### Hosting the Server

[Download the latest server jar](https://github.com/OldManAlpha/Monopoly-Deal/releases) and run it with java.exe from your Java 8 installation. This will generate data files in the same folder the jar is running in. From there, you can play by yourself with bots, or you may invite your friends to play by giving them your IP and port used for the server(example: `123.456.7.89:27599`). However, to be able to have your friends join, you will need to port-forward or use a proxy like [Ngrok](https://ngrok.com/). The default port is 27599, this may be changed in the config.txt file that is generated by the server.
</details>

### Useful Server Commands

- `start`: Deal out 5 cards to all players and start the game.
- `reset`: Puts all the cards back in the deck.
- `op [Player Name]`: Give a player server operator permissions, allowing them to execute all commands that the console could right from the in-game chat(accessible with the `T` key). `deop` can be used to revoke these permissions from a player. Simply preface your chat message with a "/" to indicate you're executing a command in-game. Example: `/start`
- `addbot <Name>`: Add a bot into your game.
- `kick [Player Name]`: Remove a player or bot from the game and return all of their cards back to the deck.
- `stop`: Closes the server.
- `edittools`: Displays and describes tools that can help you in creating your own custom deck.

<details>
<summary><b>Advanced commands</b></summary>
Some of these commands rely on Card IDs and Collection IDs. These are visible when you enable "Debug" mode using the button on the top-right of the in-game menu.

- `listplayers`: Shows information about the online players, such as their ID.
- `createcard action [Action Card Name]`: Create an action card with the given name. Make sure to put the name in one word, such as `createcard action justsayno`.
- `createcard property [Value] [Base?] [Stealable?] [Property_Name] [Colors...]`: Create a property card with the arguments. Argument details:
  - `Value`: Self explanatory. It's the value of the property card.
  - `Base`: Can be `true` or `false`. A base property means it can be used as a foundation for a color. The only properties in the vanilla deck that aren't bases are the 10-Color Property Wild Cards.
  - `Stealable`: Can be `true` or `false`. If a property is stealable, it can be targeted by Sly Deals and Forced Deals.
  - `Property_Name`: The name of the property. It must be contained in 1 word, but underscores(`_`) are converted to spaces.
  - `Colors...`: The colors the property has. These are the internal numerical colors. 0-7, starting at low quality and ending in high quality. 0 is the Brown property color and 7 is the Dark Blue property color. 8 is Railroad and 9 is Utility. Separate the numbers with spaces.
  - <b>Full example</b>: `createcard property 6 true Triple_Color_Wild_Property 0 4 9` - Creates a base property named "Triple Color Wild Property" with the value of 6M and has the colors Brown, Red, and Utility.
- `createcard money [Value]`: Create a money card with the given value.
- `listcards [Collection ID]`: Lists all of the cards in the specified collection.
- `listids [Collection ID]`: Lists all of the card ids in the specified collection.
- `collectioninfo [Collection ID]`: Tells you the amount of cards in the collection and what type of collection it is.
- `transfercard [Card ID] [Collection ID] <Index> <Time>`: Transfers a card to the specified collection, optionally specifying the index and the time(in seconds) it takes for the arrive.
- `transferall [From Collection ID] [To Collection ID] <Time>`: Transfers ALL cards from the specified collection into the other collection, optionally specifying the time(in seconds) each card takes to arrive.
- `savedeck [Deck Name]`: Create and save a deck with the given name using the cards that are currently in the deck.
- `setdeck [Deck Name]`: Swap to another deck. The name of the default deck is `vanilla`
- `nextturn`: Ends the current player's turn and goes to the next player.
- `setturn [Player ID] [Draw?]`: Set it to be the specified player's turn. Draw can be `true` or `false`, if true the player will be able to draw.

Additional undocumented commands may be seen by using the `help` command.
</details>

### Video Tutorial

[![](https://markdown-videos.vercel.app/youtube/WY_H5m0eTQ4)](https://www.youtube.com/watch?v=WY_H5m0eTQ4)


[Alternate tutorial for older version](https://www.youtube.com/watch?v=32Fa6YfK39M) explaining how to get the basic functionality setup for the client/server([Mac-specific tutorial here](https://www.youtube.com/watch?v=A9Jo7iiuOZY)). Credit to [Zyga](https://www.youtube.com/channel/UCYMOaG7Eqq1jr1-i8m48fMw) for kindly creating the videos.

## Modding Support

The server supports modding. If you're a developer and are interested in creating mods, you can find the guide [here](https://github.com/OldManAlpha/Monopoly-Deal/wiki/Modding-Guide).

### Available Mods

- **[Millionaire Edition Mod](https://github.com/OldManAlpha/Millionaire-Mod)**(made by me): Implements the functionality from the Monopoly Deal Millionaire Edition game.

## To-do

- [X] Implement customizable decks
- [X] Implement basic bots
- [X] Implement Houses/Hotels [Implemented in v0.7]
- [X] Allow for the customization of game rules [Implemented in v0.7]
- [ ] Implement action cards from other editions
- [ ] Better document server commands
- [ ] Server UI?
- [ ] Implement Monopoly Bid mode

## <img src="https://discord.com/assets/2c21aeda16de354ba5334551a883b481.png" width="80" height="80"> Discord

Come join [the Discord](https://discord.gg/9dKvSguVM4) if you'd like to chat about Monopoly Deal, make suggestions, ask for help, or look for a game.

<br><br><br><br>***This project is not affiliated with Hasbro in any way.***
