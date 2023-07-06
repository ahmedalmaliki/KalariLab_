package com.example.kalarilab;

public class MediaLink {
        private String uri;
        private boolean isChanged = false;


     public  String getUri() {
            return uri;
        }

        public void setUri(String uri)
        {
          this.uri = uri;
        }

        public void changeLink(){
            isChanged = true;
        }
        public boolean changed(){
            return isChanged;
        }
}
