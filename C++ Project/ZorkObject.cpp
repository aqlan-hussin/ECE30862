#include"ZorkObject.h"

using namespace std;

ZorkObject::ZorkObject() {

}

ZorkObject::~ZorkObject() {
	for (vector<Trigger*>::iterator it = triggers.begin(); it != triggers.end(); ++it) {
		delete *it;
	}
}