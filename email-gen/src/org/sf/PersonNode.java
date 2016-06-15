package org.sf;
 
public class PersonNode {
        private String name;
        private String email;
        private int baseExpertLevel;
       
        public void setName(String name) {
                this.name = name;
        }
        public String getName() {
                return this.name;
        }
 
 
        public String getEmail() {
                return this.email;
        }
        public void setEmail(String email) {
                this.email = email;
        }
 
        public int getBaseExpert() {
                return baseExpertLevel;
        }
        public void setBaseExpert(int baseExpertLevel) {
                this.baseExpertLevel = baseExpertLevel;
        }
       
        public String toString() {
                return this.name + ", " + this.email;
        }
       
       
        // need to hold how much of expert they are for each keyword
        // need to hold list of from/to edges
}