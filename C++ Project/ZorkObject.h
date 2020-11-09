#ifndef ZORKOBJECT_H_
#define ZORKOBJECT_H_

#include <stdio.h>

#include<string>
#include<vector>
#include"Trigger.h"

using namespace std;

class ZorkObject {

public:
	string status = "";
	vector<Trigger *> triggers;
	
	ZorkObject();
	virtual ~ZorkObject();
	
};


#endif /* ZORKOBJECT_H_ */