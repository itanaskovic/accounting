-- BUILT FOR AND TESTED WITH APACHE DERBY 10.14
CREATE TABLE Address (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  recipientDetail VARCHAR(100),
  street VARCHAR(100),
  postalCode VARCHAR(100),
  city VARCHAR(100),
  phoneNumber VARCHAR(100),
  mobileNumber VARCHAR(100),
  email VARCHAR(100),
  PRIMARY KEY(id)
);
