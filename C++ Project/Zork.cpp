#include "Zork.h"

Zork::Zork(string xmlFile) {

	Zork& newGame = *this;

	try {
		createMap(xmlFile);
	}
	catch (int error) {
		cout << "Error: Cannot parse xml file." << endl;
	}

}


Zork::~Zork() {

	for (map<string, ZorkObject*>::iterator it = objects.begin(); it != objects.end(); ++it) {
		delete it->second;
	}

}


bool Zork::createMap(string xmlFile) {

	xml_document<> doc;
	xml_node<> * init_node;

	ifstream myFile(xmlFile);

	if (!myFile.is_open()) {
		cout << "Error: Cannot open xml file." << endl;
		greenLight = false;
		return false;
	}

	vector<char> buffer((istreambuf_iterator<char>(myFile)), istreambuf_iterator<char>());
	buffer.push_back('\0');
	doc.parse<0>(&buffer[0]);

	init_node = doc.first_node();

	queue<xml_node<>*> rooms_xml;
	queue<xml_node<>*> items_xml;
	queue<xml_node<>*> containers_xml;
	queue<xml_node<>*> creatures_xml;

	xml_node<> * node = init_node->first_node();

	while (node != NULL) {

		//Current node is a room
		if (string((node->name())) == string("room")) {
			rooms_xml.push(node);
		}
		else if (string((node->name())) == string("item")) {
			items_xml.push(node);
		}
		else if (string((node->name())) == string("container")) {
			containers_xml.push(node);
		}
		else if (string((node->name())) == string("creature")) {
			creatures_xml.push(node);
		}

		node = node->next_sibling();

	}

	//Add rooms to zork map
	Room * newRoom;
	while ((rooms_xml.size()) != 0) {
		newRoom = new Room(rooms_xml.front());
		objects[newRoom->name] = newRoom;
		rooms[newRoom->name] = newRoom;
		objectTypeLookup[newRoom->name] = "room";
		rooms_xml.pop();
	}

	//Add items to zork map
	Item * newItem;
	while ((items_xml.size()) != 0) {
		//cout << "in while loop" << endl;
		newItem = new Item(items_xml.front());
		items[newItem->name] = newItem;
		objects[newItem->name] = newItem;
		objectTypeLookup[newItem->name] = "item";
		items_xml.pop();
	}
	
	//Add containers to zork map
	Container * newContainer;
	while ((containers_xml.size()) != 0){
		newContainer = new Container(containers_xml.front());
		containers[newContainer->name] = newContainer;
		objects[newContainer->name] = newContainer;
		objectTypeLookup[newContainer->name] = "container";
		containers_xml.pop();
	}

	//Add creatures to zork map
	Creature * newCreature;
	while ((creatures_xml.size()) != 0){
		newCreature = new Creature(creatures_xml.front());
		objects[newCreature->name] = newCreature;
		creatures[newCreature->name] = newCreature;
		objectTypeLookup[newCreature->name] = "creature";
		creatures_xml.pop();
	}

	greenLight = true;
	return true;
	
}


void Zork::startGame() {

	bool skip = false;
	currentRoom = "Entrance";

	cout << rooms.find(currentRoom)->second->description << endl;

	while (true) {
		skip = false;
		getline(cin, userInput);

		skip = checkAllTriggers();

		if (skip) {
			continue;
		}

		if (userInput == "") {
			cout << "Error" << endl;
			continue;
		}

		executeCommand(userInput);

		userInput = "";
		checkAllTriggers();

	}

	cout << "\nYou entered: " << userInput << endl;

}


bool Zork::checkAllTriggers() {
	
	bool skip = false;

	Room * currentRoomPtr = rooms.find(currentRoom)->second;
	Container * currentContainer;
	Item * currentItem;
	Trigger * currentTrigger;
	Creature * currentCreature;

	//Check triggers in the room
	for (std::vector<Trigger *>::iterator trigger_it = currentRoomPtr->triggers.begin(); trigger_it != currentRoomPtr->triggers.end(); ) {
		currentTrigger = *trigger_it;

		if (currentTrigger->check(*this)) {
			for (vector<string>::iterator print_it = currentTrigger->print.begin(); print_it != currentTrigger->print.end(); ++print_it) {
				cout << *print_it << endl;;
			}
			for (vector<string>::iterator action_it = currentTrigger->action.begin(); action_it != currentTrigger->action.end(); ++action_it) {
				completeAction(*action_it);
			}
			if (currentTrigger->commandFlag) {
				skip = true;
			}
			if (currentTrigger->type == "single") {
				trigger_it = currentRoomPtr->triggers.erase(trigger_it);
				delete currentTrigger;
			}
			else {
				++trigger_it;
			}		
		}
		else {
			++trigger_it;
		}
	}

	//Check items inside containers that are inside the current room, then check containers themselves
	//Iterate through containers (strings)
	for (std::map<string, string>::iterator container_it = currentRoomPtr->containers.begin(); container_it != currentRoomPtr->containers.end(); ++container_it) {
		
		//get actual container pointer
		currentContainer = containers.find(container_it->second)->second;
		
		//Iterate through items in the container
		for (std::map<string, string>::iterator item_it = currentContainer->items.begin(); item_it != currentContainer->items.end(); ++item_it){
			
			//get actual item pointer
			currentItem = items.find(item_it->second)->second;
			
			//Iterate through each trigger of item
			for (std::vector<Trigger *>::iterator trigger_it = currentItem->triggers.begin(); trigger_it != currentItem->triggers.end(); ) {
				currentTrigger = *trigger_it;
				if (currentTrigger->check(*this)) {

					for (vector<string>::iterator print_it = currentTrigger->print.begin(); print_it != currentTrigger->print.end(); ++print_it) {
						cout << *print_it << endl;;
					}
					for (vector<string>::iterator action_it = currentTrigger->action.begin(); action_it != currentTrigger->action.end(); ++action_it) {
						completeAction(*action_it);
					}
					if (currentTrigger->commandFlag) {
						skip = true;
					}
					//If its single, get rid of it
					if (currentTrigger->type == "single") {
						trigger_it = currentItem->triggers.erase(trigger_it);
						delete currentTrigger;
					}
					else {
						++trigger_it;
					}
				}
				else {
					++trigger_it;
				}
			}
		}

		//Check all container triggers
		//Iterate through each trigger of container
		for (std::vector<Trigger *>::iterator trigger_it = currentContainer->triggers.begin(); trigger_it != currentContainer->triggers.end();) {
			currentTrigger = *trigger_it;
			if (currentTrigger->check(*this)) {

				for (vector<string>::iterator print_it = currentTrigger->print.begin(); print_it != currentTrigger->print.end(); ++print_it) {
					cout << *print_it << endl;;
				}
				for (vector<string>::iterator action_it = currentTrigger->action.begin(); action_it != currentTrigger->action.end(); ++action_it) {
					completeAction(*action_it);
				}
				if (currentTrigger->commandFlag) {
					skip = true;
				}
				//If its single, get rid of it
				if (currentTrigger->type == "single") {
					trigger_it = currentContainer->triggers.erase(trigger_it);
					delete currentTrigger;
				}
				else {
					++trigger_it;
				}
			}
			else {
				++trigger_it;
			}
		}
	}

	//Check all creatures in room 
	for (std::map<string, string>::iterator creatures_it = currentRoomPtr->creatures.begin(); creatures_it != currentRoomPtr->creatures.end(); ++creatures_it) {
		
		//Get actual creature pointer
		currentCreature = creatures.find(creatures_it->second)->second;

		//Iterate through each trigger of creature
		for (std::vector<Trigger *>::iterator trigger_it = currentCreature->triggers.begin(); trigger_it != currentCreature->triggers.end();) {
			currentTrigger = *trigger_it;
			if (currentTrigger->check(*this)) {

				for (vector<string>::iterator print_it = currentTrigger->print.begin(); print_it != currentTrigger->print.end(); ++print_it) {
					cout << *print_it << endl;;
				}
				for (vector<string>::iterator action_it = currentTrigger->action.begin(); action_it != currentTrigger->action.end(); ++action_it) {
					completeAction(*action_it);
				}
				if (currentTrigger->commandFlag) {
					skip = true;
				}
				//If its single, get rid of it
				if (currentTrigger->type == "single") {
					trigger_it = currentCreature->triggers.erase(trigger_it);
					delete currentTrigger;
				}
				else {
					++trigger_it;
				}
				
			}
			else {
				++trigger_it;
			}
			
		}
	}

	//Check items in inventory
	for (std::map<string, string>::iterator inventory_it = inventory.begin(); inventory_it != inventory.end(); ++inventory_it) {
		currentItem = items.find(inventory_it->second)->second;
		//Iterate through each trigger of item
		for (std::vector<Trigger *>::iterator trigger_it = currentItem->triggers.begin(); trigger_it != currentItem->triggers.end(); ) {
			currentTrigger = *trigger_it;
			if (currentTrigger->check(*this)) {

				for (vector<string>::iterator print_it = currentTrigger->print.begin(); print_it != currentTrigger->print.end(); ++print_it) {
					cout << *print_it << endl;;
				}
				for (vector<string>::iterator action_it = currentTrigger->action.begin(); action_it != currentTrigger->action.end(); ++action_it) {
					completeAction(*action_it);
				}
				if (currentTrigger->commandFlag) {
					skip = true;
				}
				//If its single, get rid of it
				if (currentTrigger->type == "single") {
					trigger_it = currentItem->triggers.erase(trigger_it);
					delete currentTrigger;
				}
				else {
					++trigger_it;
				}
			}
			else {
				++trigger_it;
			}
		}


	
	}
	
	//Check items in room
	//Iterate through items in the room
	for (std::map<string, string>::iterator item_it = currentRoomPtr->items.begin(); item_it != currentRoomPtr->items.end(); ++item_it){

		//get actual item pointer
		currentItem = items.find(item_it->second)->second;

		//Iterate through each trigger of item
		for (std::vector<Trigger *>::iterator trigger_it = currentItem->triggers.begin(); trigger_it != currentItem->triggers.end(); ) {
			currentTrigger = *trigger_it;
			if (currentTrigger->check(*this)) {

				for (vector<string>::iterator print_it = currentTrigger->print.begin(); print_it != currentTrigger->print.end(); ++print_it) {
					cout << *print_it << endl;;
				}
				for (vector<string>::iterator action_it = currentTrigger->action.begin(); action_it != currentTrigger->action.end(); ++action_it) {
					completeAction(*action_it);
				}
				if (currentTrigger->commandFlag) {
					skip = true;
				}
				//If its single, get rid of it
				if (currentTrigger->type == "single") {
					trigger_it = currentItem->triggers.erase(trigger_it);
					delete currentTrigger;
				}
				else {
					++trigger_it;
				}
			}
			else {
				++trigger_it;
			}
		}
	}

	return skip;
}


void Zork::executeCommand(string userInput) {

	Room * currentRoomPtr = rooms.find(currentRoom)->second;
	
	//Split the command up into seperate words
	istringstream buf(userInput);
	istream_iterator<std::string> beg(buf), end;
	vector<string> commandParts(beg, end);

	//Long list of input check

	//Move to new room
	if (userInput == "n" || userInput == "e" || userInput == "s" || userInput == "w") {
		moveRoom(userInput);
	}

	//See whats in inventory
	else if (userInput == "i") {
		listInventory();
	}
	
	//take an item, either in the room or in an open container in the room
	else if( (commandParts[0] == "take") && (commandParts.size() > 1)) {
		takeItem(commandParts);
	}

	//open the exit
	else if (userInput == "open exit") {	
		openExit();
	}

	//open a container
	else if ((commandParts[0] == "open") && (commandParts.size() > 1)) {
		openContainer(commandParts);
	}

	//Read the writing on an object
	else if ((commandParts[0] == "read") && (commandParts.size() > 1)) {
		readItem(commandParts);
	}

	//Drop item
	else if ((commandParts[0] == "drop") && (commandParts.size() > 1)) {
		dropItem(commandParts);
	}

	//put item somewhere in room
	else if ((commandParts[0] == "put") && (commandParts.size() > 3)) {
		putItem(commandParts);
	}

	//try to attack
	else if ((commandParts[0] == "attack") && (commandParts.size() > 3)) {
		attackCreature(commandParts);
	}

	//Turn on an item
	else if ((commandParts.size() > 2) && (commandParts[0] == "turn") && (commandParts[1]) == "on") {
		turnOnItem(commandParts);
	}
	else {
		cout << "Error" << endl;
	}

}


void Zork::completeAction(string action) {
	istringstream buf(action);
	istream_iterator<std::string> beg(buf), end;
	vector<string> actionParts(beg, end);

	if (actionParts[0] == "Update") {
		updateStatus(actionParts);
	}
	else if (actionParts.size() > 1 && actionParts[0] == "Game" && actionParts[1] == "Over") {
		gameOver(actionParts);
	}
	else if (actionParts[0] == "Add") {
		addObject(actionParts);
	}
	else if (actionParts[0] == "Delete") {
		deleteObject(actionParts);
	}
	else {
		userInput = action;
		executeCommand(action);
	}

}


void Zork::moveRoom(string userDirection) {
	Room * currentRoomPtr = rooms.find(currentRoom)->second;

	string direction = "";
	string roomToMoveTo = "";

	if (userDirection == "n") {
		direction = "north";
	}
	else if (userDirection == "e") {
		direction = "east";
	}
	else if (userDirection == "s") {
		direction = "south";
	}
	else if (userDirection == "w") {
		direction = "west";
	}
	bool test = (currentRoomPtr->borders.find(direction)) != (currentRoomPtr->borders.end());
	if (test) {
		roomToMoveTo = (currentRoomPtr->borders.find(direction)->second);
		currentRoom = roomToMoveTo;
		cout << rooms.find(currentRoom)->second->description << endl;
	}
	else {
		cout << "Can't go that way" << endl;
	}
}

void Zork::listInventory() {
	string out = "Inventory: ";

	if (inventory.empty()) {
		cout << out << "empty" << endl;
	}
	else {
		for (std::map<string, string>::iterator it = inventory.begin(); it != inventory.end(); ++it) {
			if (it == inventory.begin()) {
				cout << out << it->second;
			}
			else {
				cout << ", " << it->second;
			}
		
		
		}
		cout << endl;
	}

}

void Zork::listContents(map<string, string> items, string containerName) {
	string out = "";

	if (items.empty()) {
		cout << containerName << " is empty." << endl;
	}
	else {
		for (std::map<string, string>::iterator it = items.begin(); it != items.end(); ++it) {
			if (it == items.begin()) {
				cout << containerName << " contains " << it->second;
			}
			else {
				cout << ", " << it->second;
			}

		}
		cout << "." << endl;
	}

}

void Zork::takeItem(vector<string> commandParts) {

	string tempString = (commandParts)[1];
	Room * currentRoomPtr = rooms.find(currentRoom)->second;
	Container * tempContainer;
	
	//Check to see if item is in the room
	if (currentRoomPtr->items.find(tempString) != currentRoomPtr->items.end()) {
		inventory[tempString] = tempString;
		currentRoomPtr->items.erase(tempString);
		cout << "Item " << tempString << " added to inventory." << endl;
	}
	//Gotta check all the containers in the room that are open as well
	else {
		bool itemFound = false;
	
		for (std::map<string, string>::iterator it = currentRoomPtr->containers.begin(); it != currentRoomPtr->containers.end(); ++it) {
			//Get real container pointer
			string containerName = it->second;
			tempContainer = containers.find(containerName)->second;

			//If the container is there, open, and has the item
			if (tempContainer != NULL && tempContainer->openFlag && tempContainer->items.find(tempString) != tempContainer->items.end()) {
				inventory[tempString] = tempString;
				tempContainer->items.erase(tempString);
				cout << "Item " << tempString << " added to inventory." << endl;
				itemFound = true;
				break;
			
			}

		
		}

		if (!itemFound) {
			cout << "Error" << endl;
		}
	}

}

void Zork::openExit() {
	//if the current room actually is an exit
	if (rooms.find(currentRoom)->second->type == "exit") {
		cout << "Game Over" << endl;
		exit(EXIT_SUCCESS);
	}
	else {
		cout << "Error" << endl;
	}
}

void Zork::openContainer(vector<string> commandParts) {
	string containerName = commandParts[1];
	Room * currentRoomPtr = rooms.find(currentRoom)->second;
	Container * containerToOpen;

	//Check to see if room has the container
	bool found = ((currentRoomPtr->containers.find(containerName)) != (currentRoomPtr->containers.end()));
	if (found) {
		containerToOpen = containers.find(containerName)->second;
		containerToOpen->openFlag = true;
		listContents(containerToOpen->items, containerName);
	}
	else {
		cout << "Error" << endl;
	}

}

void Zork::readItem(vector<string> commandParts) {
	string itemToRead = commandParts[1];
	Item * itemPtr;

	//Check if item is in inventory
	if (inventory.find(itemToRead) != inventory.end()) {
		itemPtr = items.find(itemToRead)->second;
		
		//Check to see if anything is written
		if (itemPtr->writing != "") {
			cout << itemPtr->writing << endl;
		}
		else {
			cout << "Nothing written." << endl;
		}
	}
	else {
		cout << "Error" << endl;
	}
}

void Zork::dropItem(vector<string> commandParts) {
	string itemToDrop = commandParts[1];
	Room * roomPtr = rooms.find(currentRoom)->second;

	//Check if item is in inventory
	if (inventory.find(itemToDrop) != inventory.end()) {
		roomPtr->items[itemToDrop] = itemToDrop;
		inventory.erase(itemToDrop);
		cout << itemToDrop << " dropped." << endl;

		
	}
	else {
		cout << "Error" << endl;
	}
}

void Zork::putItem(vector<string> commandParts) {
	string itemToPut = commandParts[1];
	string placeToPutItem = commandParts[3];
	Room * roomPtr = rooms.find(currentRoom)->second;
	Container * containerPtr;

	//the destination exists
	bool condition1 = (roomPtr->containers.find(placeToPutItem) != roomPtr->containers.end());
	if (condition1) { containerPtr = containers.find(placeToPutItem)->second; }
	else { cout << "Error" << endl; return; }

	//the container is open
	bool condition2 = containerPtr->openFlag;
	
	//The item is in the inventory
	bool condition3 = inventory.find(itemToPut) != inventory.end();

	//If all conditions were met
	if (condition1 && condition2 && condition3) {
		containerPtr->items[itemToPut] = itemToPut;
		inventory.erase(itemToPut);
		cout << "Item " << itemToPut << " added to " << placeToPutItem << "." << endl;


	}
	else {
		cout << "Error" << endl;
	}
}

void Zork::attackCreature(vector<string> commandParts) {
	string creatureName = commandParts[1];
	string weapon = commandParts[3];
	Creature * creatureToAttack;
	Room * roomPtr = rooms.find(currentRoom)->second;
	
	//if creature is in current room
	if (roomPtr->creatures.find(creatureName) != roomPtr->creatures.end()) {
		creatureToAttack = creatures.find(creatureName)->second;

		//If the weapon is in inventory
		if (inventory.find(weapon) != inventory.end()) {
			//If attack is successful
			if (creatureToAttack->attack(*this, weapon)) {
				cout << "You assault the " << creatureName << " with the " << weapon << "." << endl;

				//For each attack message in creature
				for (vector<string>::iterator it = creatureToAttack->print.begin(); it != creatureToAttack->print.end(); ++it) {
					//Print out that message
					cout << *it << endl;
				}

				//For each action in creature attack
				for (vector<string>::iterator it = creatureToAttack->action.begin(); it != creatureToAttack->action.end(); ++it) {
					//Complete that action
					completeAction(*it);
				}

			}
			else {
				cout << "Error" << endl;
			}
		}
		else {
			cout << "Error" << endl;
		}
	}
	else {
		cout << "Error" << endl;
	}
}

void Zork::turnOnItem(vector<string> commandParts) {
	string itemToTurnOn = commandParts[2];
	Room * roomPtr = rooms.find(currentRoom)->second;
	Item * itemPtr;

	//Check if item is in inventory
	if (inventory.find(itemToTurnOn) != inventory.end()) {
		itemPtr = items.find(itemToTurnOn)->second;
		cout << "You activate the " << itemToTurnOn << "." << endl;

		//For each turnon message in item
		for (vector<string>::iterator it = itemPtr->onMessages.begin(); it != itemPtr->onMessages.end(); ++it) {
			//Print out that message
			cout << *it << endl;
		}

		//For each turnon action in item
		for (vector<string>::iterator it = itemPtr->onActions.begin(); it != itemPtr->onActions.end(); ++it) {
			//Complete that action
			completeAction(*it);
		}
	}
	else {
		cout << "Error" << endl;
	}
}


void Zork::updateStatus(vector<string> actionParts) {
	//Find out what what type of object needs updating
	string object = actionParts[1];
	string newStatus = actionParts[3];
	string objectType = objectTypeLookup.find(object)->second;

	if (objectType == "room") {
		rooms.find(object)->second->status = newStatus;
	}
	else if (objectType == "container") {
		containers.find(object)->second->status = newStatus;
	}
	else if (objectType == "creature") {
		creatures.find(object)->second->status = newStatus;
	}
	else if (objectType == "item") {
		items.find(object)->second->status = newStatus;
	}

}

void Zork::gameOver(vector<string> actionParts) {
	cout << "Victory!" << endl;
	exit(EXIT_SUCCESS);
}


void Zork::addObject(vector<string> actionParts) {
	string placeToAddItem = actionParts[3];
	string object = actionParts[1];
	string objectType = objectTypeLookup.find(object)->second;
	string placeToAddItemType = objectTypeLookup.find(placeToAddItem)->second;

	if (placeToAddItemType == "room") {
		Room * roomToAddItemTo = rooms.find(placeToAddItem)->second;
		if (objectType == "item") {
			roomToAddItemTo->items[object] = object;
		}
		else if (objectType == "creature") {
			roomToAddItemTo->creatures[object] = object;
		}
		else if (objectType == "container") {
			roomToAddItemTo->containers[object] = object;
		}
		else {
			cout << "Error" << endl;
		}
	}
	else if (placeToAddItemType == "container") {
		Container * containerToAddItemTo = containers.find(placeToAddItem)->second;
		if (objectType == "item") {
			containerToAddItemTo->items[object] = object;
		}
		else {
			cout << "Error" << endl;
		}
	}

	else {
		cout << "Error" << endl;
	}
}

void Zork::deleteObject(vector<string> actionParts) {
	string object = actionParts[1];
	string objectType = objectTypeLookup.find(object)->second;

	if (objectType == "room") {
		Room * tempRoom;
		//For each of the rooms
		for (map<string, Room*>::iterator it = rooms.begin(); it != rooms.end(); ++it) {
			tempRoom = it->second;

			//For each of the borders in a room
			for (map<string, string>::iterator it2 = tempRoom->borders.begin(); it2 != tempRoom->borders.end(); ++it2) {
				//If the border matches the room that is going to be deleted
				if (object == it2->second) {
					tempRoom->borders.erase(object);
				}
			}
		}

	}
	else if (objectType == "item") {
		Room * tempRoom;
		//For each of the room
		for (map<string, Room*>::iterator it = rooms.begin(); it != rooms.end(); ++it) {
			tempRoom = it->second;
			//If the room has the item in it
			if (tempRoom->items.find(object) != tempRoom->items.end()) {
				tempRoom->items.erase(object);
			}
		}
		Container * tempContainer;
		//For each of the containers
		for (map<string, Container*>::iterator it = containers.begin(); it != containers.end(); ++it) {
			tempContainer = it->second;
			//If the container has the item in it
			if (tempContainer->items.find(object) != tempContainer->items.end()) {
				tempContainer->items.erase(object);
			}
		}
	}
	else if (objectType == "container") {
		Room * tempRoom;
		//For each of the room
		for (map<string, Room*>::iterator it = rooms.begin(); it != rooms.end(); ++it) {
			tempRoom = it->second;
			//If the room has the container in it
			if (tempRoom->containers.find(object) != tempRoom->containers.end()) {
				tempRoom->containers.erase(object);
			}
		}
		
	}
	else if (objectType == "creature") {
		Room * tempRoom;
		//For each of the room
		for (map<string, Room*>::iterator it = rooms.begin(); it != rooms.end(); ++it) {
			tempRoom = it->second;
			//If the room has the creature in it
			if (tempRoom->creatures.find(object) != tempRoom->creatures.end()) {
				tempRoom->creatures.erase(object);
			}
		}
	}



}