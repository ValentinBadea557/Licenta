package ro.mta.licenta.badea.temporalUse;

import ro.mta.licenta.badea.models.ProjectModel;

public class SenderText {
    private static String data = new String();
    private ProjectModel project = new ProjectModel();

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public SenderText() {
    }

    public static String getData() {
        return data;
    }

    public static void setData(String data) {
        SenderText.data = data;
    }
}
