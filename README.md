# ChitChat

## Usage

Start the server by double-clicking on the `ChitChatServer.jar` file.
The default port is `6667`, an alternative port can be specified by calling

`java -jar /path/to/ChitChatServer.jar PORT_NUMBER`

Start the client either by double-clicking or starting a shell and calling

`java -jar /path/to/ChitChatClient.jar`

Enter server's hostname and service port and choose a username. The application will show an error message if the username is taken.

## Client Interfaces

### Setting up your own Client

* Open connection to server over a `Socket` object.
* Message server with chosen username as a `String`.
* If username is taken, server will reply with `#IDTAKEN` and close the connection.
* If login succeeds, server replies with `$user has entered the chat.`
* Server will broadcast a list of all users currently online, formatted as `#USERLIST;user1;...;userN`

Clients must then create a new `ChatHandler` thread and pass their connection socket and their implementations of the `IMessageInput` and `IMessageOutput` interfaces.
For messages you can expect from the server / can send to the server, see the following section **Server Messages**.

### Server Messages

* **Incoming Whispers**: Formatted as `#WHISPER;source_user;message`.
* **Outgoing Whispers**: Must be formatted as `#WHISPER;destination_user;message`. Server replies with an error message if the user does not exist.
* **User Broadcast Messages**: Formatted as `username;message`
* **Outgoing User Broadcast Messages**: Sent as string without modification.
* **Server Broadcast Messages**: Received as string without modification.
