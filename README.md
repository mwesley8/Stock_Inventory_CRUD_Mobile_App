# Stock_Inventory_CRUD_Mobile_App

I worked as a restaurant manager for a number of years. We conducted inventory twice a week. I was happy when they finally upgraded the process to using an iPad. The inventory application sounded like an interesting project to explore.

(Scenario)

I was recently hired as the newest member of my mobile application development team at Mobile2App Company. The Inventory App is one of my latest client projects. The client envisions an application that will allow them to securely log in and perform CRUD operations. If the user does not have an account set up, then the application provides functionality for the user to create an account.

After logging in, the user is able to perform four different use cases. The user would like the ability to view the entire inventory with specific categories of interest. Additionally, the user would like to add and remove items, by specific ID, from the inventory. A specific item in the inventory shall be mutable (increasable, decreasable). Finally, the client would like to be notified when an inventory item has been reduced to zero.

After completion of this project, the user is able to:

Login, the login button visibility dynamically changes depending on text length.

![image](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/efb97448-c95a-4a85-97d2-08fb85e8ac01)

Create an account and login.

![Create Account Screen](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/640c7aeb-72d9-4ce8-a23a-168c1c1051c1)

View current inventory in a recycle view.

![Stock Inventory](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/7a142679-040a-4977-abfc-2a6dc96b586c)

Interaction with the Recycler View.

Press the item id in the row to view the item and quantity in the header.

![Stock Inventory Header Change](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/79317601-905f-4705-8c0b-823a673e2ddb)

Press the item or quantity in the row to update item.

![Update Screen](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/21a0457f-ea11-4296-841e-416c1ca88d4a)

Press the trash can icon to delete an item.

![Delete Screen](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/f288ce81-d6e7-4f45-87ba-b1849023d907)

Press floating action button to add an item

![Add Item Screen](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/cf52c7a5-6ded-4ea1-af03-2abd647d73a6)

Press update button to allow permission to send SMS

![SMS Permission Dialog Box](https://github.com/mwesley8/Stock_Inventory_CRUD_Mobile_App/assets/105822088/deb31ad3-61a1-4124-8ba1-3081448598cf)

Press the logout action button in the menu to logout.

The most difficult challenge during this project was the integration of SQL with Android Studio. I could not consistently open and close the working database. During my research, I was able to find a wrapper that was able to find the absolute path to the external databases. Otherwise, it was enjoyable to learn mobile architecture.
