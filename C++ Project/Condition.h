#ifndef CONDITION_H_
#define CONDITION_H_

#include <stdio.h>
#include <string>
#include <vector>
#include <map>

class Zork;

using namespace std;

class Condition {
public:
	string object = "";
	virtual bool check(Zork&) = 0;
	virtual ~Condition(){};

};

#endif /* CONDITION_H_ */
