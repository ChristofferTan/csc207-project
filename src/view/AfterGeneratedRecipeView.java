    package view;

    import entity.MealType;
    import entity.Recipe;
    import interface_adapters.ViewManagerModel;
    import interface_adapters.add_favorite_recipe.AddFavoriteRecipeController;
    import interface_adapters.after_generated_recipe.AfterGeneratedRecipeState;
    import interface_adapters.after_generated_recipe.AfterGeneratedRecipeViewModel;
    import interface_adapters.generate_recipe.GenerateRecipeState;
    import interface_adapters.generate_recipe.GenerateRecipeViewModel;
    import interface_adapters.save_recipe.SaveRecipeController;

    import javax.imageio.ImageIO;
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.KeyEvent;
    import java.awt.event.KeyListener;
    import java.beans.PropertyChangeEvent;
    import java.beans.PropertyChangeListener;
    import java.io.IOException;
    import java.net.URL;
    import java.time.DayOfWeek;

    public class AfterGeneratedRecipeView extends JPanel implements ActionListener, PropertyChangeListener {
        public final String viewName = "after generated recipe";
        private final AfterGeneratedRecipeViewModel afterGeneratedRecipeViewModel;
        private final GenerateRecipeViewModel generateRecipeViewModel;
        private final SaveRecipeController saveRecipeController;
        private final AddFavoriteRecipeController addFavoriteRecipeController;
        private final ViewManagerModel viewManagerModel;

        JLabel recipeLabel, recipeURL, servings, calories, preparation, imageLabel;
        private final JTextField mealTypeInputField = new JTextField(15);
        private final JTextField dayInputField = new JTextField(15);
        final JButton submit, favorite, back;

        public AfterGeneratedRecipeView(AfterGeneratedRecipeViewModel afterGeneratedRecipeViewModel, GenerateRecipeViewModel generateRecipeViewModel, SaveRecipeController saveRecipeController, AddFavoriteRecipeController addFavoriteRecipeController, ViewManagerModel viewManagerModel) {
            this.afterGeneratedRecipeViewModel = afterGeneratedRecipeViewModel;
            this.generateRecipeViewModel = generateRecipeViewModel;
            this.saveRecipeController = saveRecipeController;
            this.addFavoriteRecipeController = addFavoriteRecipeController;
            this.viewManagerModel = viewManagerModel;
            afterGeneratedRecipeViewModel.addPropertyChangeListener(this);

            // recipeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            recipeLabel = new JLabel();
            recipeURL = new JLabel();

            Image image = null;
            try {
                Recipe recipe = afterGeneratedRecipeViewModel.getState().getRecipe();
                if (recipe != null && recipe.getImagePath() != null) {
                    URL imageURL = new URL(recipe.getImagePath());
                    Image originalImage = ImageIO.read(imageURL);

                    int maxWidth = 200;
                    int maxHeight = 200;

                    Image resizedImage = originalImage.getScaledInstance(
                            maxWidth,
                            maxHeight,
                            Image.SCALE_SMOOTH
                    );

                    imageLabel.setIcon(new ImageIcon(resizedImage));
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }

            if (image != null) {
                imageLabel = new JLabel(new ImageIcon(image));
            } else {
                imageLabel = new JLabel("Image Not Available");
            }

            JLabel servingsInfo = new JLabel(AfterGeneratedRecipeViewModel.SERVINGS_LABEL);
            servings = new JLabel();

            JLabel caloriesInfo = new JLabel(AfterGeneratedRecipeViewModel.CALORIES_INFO_LABEL);
            calories = new JLabel();

            JLabel preparationInfo = new JLabel(AfterGeneratedRecipeViewModel.PREPARATION_LABEL);
            preparation = new JLabel();

            JLabel addToPlannerInfo = new JLabel(AfterGeneratedRecipeViewModel.ADD_TO_PLANNER_LABEL);
            JLabel dayInPlannerInfo = new JLabel(AfterGeneratedRecipeViewModel.ADD_TO_PLANNER_DAY_LABEL);

            JPanel servingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            servingsPanel.add(servingsInfo);
            servingsPanel.add(servings);

            JPanel caloriesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            caloriesPanel.add(caloriesInfo);
            caloriesPanel.add(calories);

            JPanel preparationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            preparationPanel.add(preparationInfo);
            preparationPanel.add(preparation);

            JPanel addToPlannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            addToPlannerPanel.add(addToPlannerInfo);
            addToPlannerPanel.add(mealTypeInputField);
            addToPlannerPanel.add(dayInPlannerInfo);
            addToPlannerPanel.add(dayInputField);

            JPanel buttons = new JPanel();
            submit = new JButton(AfterGeneratedRecipeViewModel.SUBMIT_BUTTON_LABEL);
            favorite = new JButton(AfterGeneratedRecipeViewModel.ADD_TO_FAVORITE_LIST_BUTTON_LABEL);
            back = new JButton(AfterGeneratedRecipeViewModel.BACK_BUTTON_LABEL);
            buttons.add(submit);
            buttons.add(favorite);
            buttons.add(back);

            submit.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource().equals(submit)) {
                                AfterGeneratedRecipeState currentState = afterGeneratedRecipeViewModel.getState();
                                // System.out.println("Hari " + currentState.getMealType());
                                // System.out.println("Tipe makanan " + currentState.getMealType());

                                saveRecipeController.execute(
                                        currentState.getUsername(),
                                        currentState.getDayInPlanner(),
                                        currentState.getMealType(),
                                        currentState.getRecipe());
                            }
                        }
                    }
            );

            favorite.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource().equals(favorite)) {
                                AfterGeneratedRecipeState currentState = afterGeneratedRecipeViewModel.getState();

                                addFavoriteRecipeController.execute(
                                        currentState.getUsername(),
                                        currentState.getRecipe().getLabel());
                            }
                        }
                    }
            );

            back.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource().equals(back)) {
                                AfterGeneratedRecipeState currentState = afterGeneratedRecipeViewModel.getState();

                                GenerateRecipeState generateRecipeState = generateRecipeViewModel.getState();
                                generateRecipeState.setUsername(currentState.getUsername());

                                generateRecipeViewModel.setState(generateRecipeState);
                                generateRecipeViewModel.firePropertyChanged();
                                viewManagerModel.setActiveView(generateRecipeViewModel.getViewName());
                                viewManagerModel.firePropertyChanged();
                            }
                        }
                    }
            );

            mealTypeInputField.addKeyListener(
                    new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            AfterGeneratedRecipeState currentState = afterGeneratedRecipeViewModel.getState();
                            String text = mealTypeInputField.getText();
                            currentState.setMealType(MealType.fromString(text));
                            afterGeneratedRecipeViewModel.setState(currentState);
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {

                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    }
            );

            dayInputField.addKeyListener(
                    new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            AfterGeneratedRecipeState currentState = afterGeneratedRecipeViewModel.getState();
                            String text = dayInputField.getText();
                            System.out.println(text);
                            currentState.setDayInPlanner(fromStringToDayofWeek(text));
                            afterGeneratedRecipeViewModel.setState(currentState);
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {

                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    }
            );

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(recipeLabel);
            this.add(imageLabel);
            this.add(servingsPanel);
            this.add(caloriesPanel);
            this.add(preparationPanel);
            this.add(addToPlannerPanel);
            this.add(buttons);
        }

        public static DayOfWeek fromStringToDayofWeek(String stringValue) {
            // System.out.println("Hasil ketikan: " + stringValue);
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                if (dayOfWeek.toString().equals(stringValue)) {
                    return dayOfWeek;
                }
            }
            return null;
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AfterGeneratedRecipeState state = (AfterGeneratedRecipeState) evt.getNewValue();
            recipeLabel.setText(state.getRecipe().getLabel());
            recipeURL.setText(state.getRecipe().getRecipeUrl());
            servings.setText(String.valueOf(state.getRecipe().getYield()));
            calories.setText(String.valueOf(state.getRecipe().getCalories()));
            preparation.setText(String.valueOf(state.getRecipe().getPreparationTime()));

            if (state.getRecipe() != null) {
                updateImage(state.getRecipe().getImagePath());
            }
        }

        private void updateImage(String imagePath) {
            try {
                if (imagePath != null) {
                    URL imageURL = new URL(imagePath);
                    Image originalImage = ImageIO.read(imageURL);

                    int maxWidth = 200;
                    int maxHeight = 200;

                    Image resizedImage = originalImage.getScaledInstance(
                            maxWidth,
                            maxHeight,
                            Image.SCALE_SMOOTH
                    );

                    imageLabel.setIcon(new ImageIcon(resizedImage));
                    imageLabel.setText("");
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("Image Not Available");
                }
            } catch (IOException | IllegalArgumentException | NullPointerException exp) {
                exp.printStackTrace();
            }
        }
    }
