# capstone495game
A short webcam-controlled game, built for my CSC495 capstone project.

Detailed description in the .docx file.

###### Demo (Mouse controls only) - FullDemoMouseOnly.jar
- Basic controls: Mouse over icons to open menus/select objects to give to your rock.
- Mousing over a menu icon (bottom row) while the corresponding menu is open closes the menu.
- If you are holding an object, you can discard it with the trash can or apply it to the rock.

###### SickDemo rules
- Press Q to toggle sickness on/off. When the rock is sick, its happiness will decrease by 3 points every 2 seconds.
- Mouse over the white box to open the medicine menu
- When the menu is open, mouse over the pill to grab it
- Bring the pill to the rock using the mouse.
- If the pill is given to the rock while it is sick, its happiness will increase by 15, and its sickness will be cured
- If the rock is not sick, the pill will lower its happiness by 15.

###### PoopDemo rules
- Press Q to spawn poop. 
- Mouse over poop to dispose of it.
- Press R to reset the meter to 100%.
- Each poop spawned decreases the meter by 1 point.
- For each individual poop on-screen, the meter will drop 1 point every 5 seconds since that poop spawned.

###### FoodDemo rules
- Stomach meter decreases by 1 every 0.2 seconds.
- When stomach meter is below 40 points, happiness meter decreases by 2 every 0.2 seconds.
- Mouse over the food box to open the food menu.
- Mouse over the red X to close the food menu or mouse over the bread to take a piece of food.
- Once you are holding bread on the cursor, move the cursor to the rock to feed him.
- Feeding the rock increases the stomach and happiness meters by 40.

###### ToysDemo rules
- Energy meter refills every 0.5 seconds.
- Hover over the white box in the bottom left corner to open the Toy Box.
- Hover over a toy to pick it up. Drag it to the rock to play with it.
- Playing with the rock decreases its energy by 30.
- If the rock's energy is above 50, playing with it will boost its happiness by 10.
- Using the rock's favorite toy (tennis ball) to play will boost its happiness by an additional 10.
- Playing with the rock while it is tired (energy < 50) lowers happiness by 10. 