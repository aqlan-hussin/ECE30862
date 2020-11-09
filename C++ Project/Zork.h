#ifndef ZORK_H_
#define ZORK_H_

#include <iostream>
#include <string>
#include <stdio.h>
#include "rapidxml.hpp"
#include <fstream>
#include <vector>
#include <queue>  
#include "Item.h"
#include "Container.h"
#include "Creature.h"
#include <map>
#include "Room.h"
#include <sstream>
#include <iterator>

using namespace std;
using namespace rapidxml;

class Zork {

public:
	bool greenLight = false;
	
	map<string,ZorkObject*> objects; // These are the only pointers delete will need to be called on
	
	map<string,Item*> items; //These items are in objects
	
	map<string,string> inventory;
	map<string,Container*> containers; //these containers are in objects
	map<string,Creature*> creatures;
	map<string,Room*> rooms;
	map<string,string> objectTypeLookup;
	
	Zork(string);
	//Zork(Zork&);
	virtual ~Zork();
	void startGame();
	

	string userInput = "";
	string currentRoom = "";
	
	
private:

	bool createMap(string xmlFile);
	
	//void seperatateTopXmlNodes(xml_node<> *, queue<xml_node<> *>&, queue<xml_node<> *>&, queue<xml_node<> *>&, queue<xml_node<> *>&);

	bool checkAllTriggers();

	void executeCommand(string);

	void completeAction(string);

	void moveRoom(string);

	void listInventory();

	void listContents(map<string, string>, string);

	void takeItem(vector<string>);

	void openExit();

	void openContainer(vector<string>);
	
	void readItem(vector<string>);

	void dropItem(vector<string>);

	void putItem(vector<string>);

	void turnOnItem(vector<string>);

	void attackCreature(vector<string>);

	void updateStatus(vector<string>);

	void gameOver(vector<string>);

	void addObject(vector<string>);

	void deleteObject(vector<string>);

};


#endif /* ZORK_H_ */