package com.ihm18.bookmaker.presentation.pagescomponent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;

import com.ihm18.bookmaker.businessobject.Album;
import com.ihm18.bookmaker.businessobject.IHMImage;
import com.ihm18.bookmaker.businessobject.Page;
import com.ihm18.bookmaker.presentation.albumdetailcomponent.AlbumDetailModel;
import com.ihm18.bookmaker.presentation.utility.SoundPlayer;
import com.ihm18.bookmaker.presentation.utility.Utility;
import com.ihm18.bookmaker.service.ImageService;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

/**
 *
 * @author oualidqannouf
 */
public class PagesPresenter implements Initializable {

	@Inject
	private AlbumDetailModel albumDetailModel;

	@Inject
	private Utility utility;
	@Inject
	private SoundPlayer soundPlayer;
	@Inject
	ImageService imageService;

	private Album album;

	@FXML
	private GridPane pagesContainer;
	@FXML
	private ImageView leftSideImageView;
	@FXML
	private ImageView rightSideImageView;
	@FXML
	private Label leftPageNumberLabel;
	@FXML
	private Label rightPageNumberLabel;
	@FXML
	private GridPane leftGridPane;
	@FXML
	private GridPane rghtGridPane;

	@FXML
	private ImageView imageView_l_0_0;
	@FXML
	private ImageView imageView_l_0_1;
	@FXML
	private ImageView imageView_l_1_0;
	@FXML
	private ImageView imageView_l_1_1;
	@FXML
	private ImageView imageView_l_2_0;
	@FXML
	private ImageView imageView_l_2_1;
	@FXML
	private ImageView imageView_r_0_0;
	@FXML
	private ImageView imageView_r_0_1;
	@FXML
	private ImageView imageView_r_1_0;
	@FXML
	private ImageView imageView_r_1_1;
	@FXML
	private ImageView imageView_r_2_0;
	@FXML
	private ImageView imageView_r_2_1;

	private ImageView[] leftImageViews;
	private ImageView[] rightImageViews;

	private int activePageNumber = 0;

	private int pagesNumber;

	public void launch() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		leftImageViews = new ImageView[] { imageView_l_0_0, imageView_l_0_1, imageView_l_1_0, imageView_l_1_1,
				imageView_l_2_0, imageView_l_2_1 };
		rightImageViews = new ImageView[] { imageView_r_0_0, imageView_r_0_1, imageView_r_1_0, imageView_r_1_1,
				imageView_r_2_0, imageView_r_2_1 };
		// On crée les pages dynamiquement
		album = albumDetailModel.getAlbum();
		pagesNumber = album.getPages().size();
		activePageNumber = (pagesNumber > 0) ? activePageNumber = 1 : 0;
		updateBackground();

		leftGridPane.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
				System.out.println("here");

				event.consume();
			}
		});

		leftGridPane.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("dropped");
				Dragboard db = event.getDragboard();
				final boolean isAccepted = db.hasFiles()
						&& (db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
								|| db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
								|| db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg"));

				if (isAccepted) {
					System.out.println("accepted");
					File imageFile = db.getFiles().get(0);
					try {
						acceptImage(imageFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					event.consume();
				}
			}

		});
	}

	public void acceptImage(File imageFile) throws IOException {
		IHMImage image = new IHMImage(imageFile.getName(), "", LocalDateTime.now());
		image.setFile(imageFile);
		Page page = album.getPages().get(activePageNumber - 1);
		page.getImages().add(image);
		image.setPage(page);
		fillViewWithPage(true, activePageNumber);
	}

	public void addPage(ActionEvent event) {
		attachPage();
		activePageNumber = (pagesNumber % 2 == 0) ? pagesNumber - 1 : pagesNumber;
		updateBackground();

	}

	/**
	 * Cette fonction permet d'actualiser les images d'une page.
	 * 
	 * @param isLeftView
	 *            true s'il s'agit de la vue à gauche(page à gauche)
	 * @param pageNumber
	 *            la page de l'album
	 * @throws IOException
	 */
	public void fillViewWithPage(boolean isLeftView, int pageNumber) throws IOException {
		System.out.println("fill");
		Page page = album.getPages().get(pageNumber - 1);
		if (isLeftView)
			for (int i = 0; i < page.getImages().size(); i++) {
				IHMImage ihmImage = page.getImages().get(i);
				Image image = new Image(FileUtils.openInputStream(ihmImage.getFile()));

				leftImageViews[i].setImage(image);
			}
	}

	private void attachPage() {

		Page page = new Page();
		page.setNumber(++pagesNumber);
		page.setAlbum(album);
		album.addPage(page);
	}

	private void updateBackground() {

		resetBackground();

		if (activePageNumber == pagesNumber && pagesNumber != 0) {
			Image image = utility.getFXImage("left-page.png");
			leftSideImageView.setImage(image);

			leftPageNumberLabel.setText(String.valueOf(pagesNumber));
			rightPageNumberLabel.setText("");
		} else if (activePageNumber < pagesNumber) {
			Image image = utility.getFXImage("left-page.png");
			leftSideImageView.setImage(image);
			image = utility.getFXImage("right-page.png");
			rightSideImageView.setImage(image);

			leftPageNumberLabel.setText(String.valueOf(activePageNumber));
			rightPageNumberLabel.setText(String.valueOf(activePageNumber + 1));
		}
	}

	private void resetBackground() {
		Image image = utility.getFXImage("left-side.png");
		leftSideImageView.setImage(image);
		image = utility.getFXImage("right-side.png");
		rightSideImageView.setImage(image);
	}

	public void turnLeft(ActionEvent event) {
		if (activePageNumber - 2 >= 1) {
			activePageNumber = activePageNumber - 2;
			soundPlayer.playSound("page-flip");
		}
		updateBackground();
	}

	public void turnRight(ActionEvent event) {
		if (activePageNumber + 2 <= pagesNumber) {
			soundPlayer.playSound("page-flip");
			activePageNumber = activePageNumber + 2;
		}
		updateBackground();
	}

}