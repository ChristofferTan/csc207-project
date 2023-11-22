package use_case.delete_favorite_recipe;

import data_access.FileRecipeDataAccessObject;
import entity.Recipe;
import use_case.add_favorite_recipe.AddFavoriteRecipeOutputBoundary;

public class DeleteFavoriteRecipeInteractor implements DeleteFavoriteRecipeInputBoundary{
    final DeleteFavoriteRecipeDataAccessInterface userDataAccessObject;
    final FileRecipeDataAccessObject fileRecipeDataAccessObject;
    final DeleteFavoriteRecipeOutputBoundary presenter;

    public DeleteFavoriteRecipeInteractor(DeleteFavoriteRecipeDataAccessInterface userDataAccessObject, FileRecipeDataAccessObject fileRecipeDataAccessObject, DeleteFavoriteRecipeOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.fileRecipeDataAccessObject = fileRecipeDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteFavoriteRecipeInputData deleteFavoriteRecipeInputData) {
        if (!userDataAccessObject.isExists(deleteFavoriteRecipeInputData.getUsername(), deleteFavoriteRecipeInputData.getLabel())) {
            presenter.prepareFailView("You do not have this recipe in My Favorite Recipe");
        }
        else {
            userDataAccessObject.deleteFavoriteRecipe(deleteFavoriteRecipeInputData.getUsername(), deleteFavoriteRecipeInputData.getLabel());

            DeleteFavoriteRecipeOutputData deleteFavoriteRecipeOutputData = new DeleteFavoriteRecipeOutputData(deleteFavoriteRecipeInputData.getLabel());
            presenter.prepareSuccessView(deleteFavoriteRecipeOutputData);
        }
    }
}