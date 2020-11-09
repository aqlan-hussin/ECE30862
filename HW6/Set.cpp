#include "Set.h"
#include <string>
#include <iostream>

using namespace std;

Set::Set(int n) : l(n) {
	this -> slots = new bool[l+1];
	copyCount = 0;
	for (int i = 0; i < l+1; i++) {
		this->slots[i] = false;
	}
}

Set::Set(const Set& s) : l(s.l), copyCount(s.copyCount+1) {
	this->slots = new bool[l+1];
	for (int i = 0; i < l+1; i++) {
			this->slots[i] = s.slots[i];
	}
}

Set::~Set() {}

Set& Set::operator+(const int n){
	if (n < this->l+1) {
		this->slots[n] = true;
	}
	return *this;
}

Set& Set::operator-(const int n){
	if (n < this->l+1) {
		this->slots[n] = false;
	}
	return *this;
}

Set Set::operator&(Set& s) {
	if (this->l != s.l) {
		return s;
	}
	int n = this->l;
	Set newSet = Set(n);
	for (int i = 0; i < this->l+1; i++) {
		if (this->slots[i] && s.slots[i]) {
			newSet = newSet + i;
		}
	}
	return newSet;
}

Set Set::operator~() {
	int n = this->l;
	Set newSet = Set(n);
	for (int i = 0; i < this->l+1; i++) {
		newSet.slots[i] = ~this->slots[i];
	}
	return newSet;
}

Set Set::operator/(Set& s) {
	if (this->l != s.l) {
		return s;
	}
	int n = this->l;
	Set newSet = Set(n);
	for (int i = 0; i < this->l+1; i++) {
		if (this->slots[i]==true && s.slots[i]==false) {
			newSet = newSet + i;
		}
	}
	return newSet;
}

ostream& operator<<(ostream& os, const Set& s) {
	string myStr = "";
	for (int i = 0; i < s.l+1; i++) {
		if (s.slots[i] == true) {
			myStr += to_string(i) + ", ";
		}
	}
	myStr = myStr.substr(0,myStr.size()-1);
	myStr = myStr.substr(0,myStr.size()-1);
	os << myStr;
	return os;
}

int Set::getCopyCount() const {
	return copyCount;
}

