# Parallel-Processing-Real-Time-Messaging-Program
My messaging application incoroprates RMI. The messaging interface has two methods one for writing messages and another for 
reading messages. The writemessage takes in the user who sends the message the actual message and who the sender is sending 
the message to. These are all string varaibales. The readmessage takes in who sent the message and who the contact is as string 
variables.

The server handles these two methods separately. Firstly the Writing method stores the messages by the user who sent it and 
who they were sending to. That is the key for the hashtable and the values are a string list containing each message. The reading 
message method gets the key by swapping the contact and who they are talking to around so they can access the key that was made 
when the message was written. 

For the client I wanted the messages to be able to be updated in real time so I decided to use an executor service to use 
two processes one for writing messages so it could deal with input from the user on the command line and thus not halt the 
other process while it was waiting for input. Therefore the other process reads in new messages, which it checks for new 
messages every 5 seconds. I also wanted the user to be able to change between contacts so all they do is type in "Contact" 
and they can choose a new contact. To exit the application type in "Exit". The messaging application still has two minor 
problems if the user takes too long to input a new contact the messages from the previous contact will start coming through. 
And lastly when you go to a contact you have already messaged the messages display twice. (I am working on these problems).
