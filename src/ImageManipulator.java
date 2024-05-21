import java.io.IOException;
import java.util.ArrayList;

/**
 * Static utility class that is responsible for transforming the images.
 * Each function (or at least most functions) take in an Image and return
 * a transformed image.
 */
public class ImageManipulator {
    /**
     * Loads the image at the given path
     * @param path path to image to load
     * @return an Img object that has the given image loaded
     * @throws IOException
     */
    public static Img LoadImage(String path) throws IOException {
        return new Img(path);
    }

    /**
     * Saves the image to the given file location
     * @param image image to save
     * @param path location in file system to save the image
     * @throws IOException
     */
    public static void SaveImage(Img image, String path) throws IOException {
        image.Save("png", path);
    }

    /**
     * Converts the given image to grayscale (black, white, and gray). This is done
     * by finding the average of the RGB channel values of each pixel and setting
     * each channel to the average value.
     * @param image image to transform
     * @return the image transformed to grayscale
     */
    public static Img ConvertToGrayScale(Img image) {
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                int avg = (currentPix.GetRed()+currentPix.GetGreen()+currentPix.GetBlue())/3;
                currentPix.SetRed(avg);
                currentPix.SetGreen(avg);
                currentPix.SetBlue(avg);
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }

    /**
     * Inverts the image. To invert the image, for each channel of each pixel, we get
     * its new value by subtracting its current value from 255. (r = 255 - r)
     * @param image image to transform
     * @return image transformed to inverted image
     */
    public static Img InvertImage(Img image) {
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                currentPix.SetRed(255-currentPix.GetRed());
                currentPix.SetGreen(255-currentPix.GetGreen());
                currentPix.SetBlue(255-currentPix.GetBlue());
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }

    /**
     * Converts the image to sepia. To do so, for each pixel, we use the following equations
     * to get the new channel values:
     * r = .393r + .769g + .189b
     * g = .349r + .686g + .168b
     * b = 272r + .534g + .131b
     * @param image image to transform
     * @return image transformed to sepia
     */
    public static Img ConvertToSepia(Img image) {
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                int r = currentPix.GetRed();
                int g = currentPix.GetGreen();
                int b = currentPix.GetBlue();
                currentPix.SetRed((int)(.393*r + .769*g + .189*b));
                currentPix.SetGreen((int)(.349*r + .686*g + .168*b));
                currentPix.SetBlue((int)(.272*r + .534*g + .131*b));
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }

    /**
     * Creates a stylized Black/White image (no gray) from the given image. To do so:
     * 1) calculate the luminance for each pixel. Luminance = (.299 r^2 + .587 g^2 + .114 b^2)^(1/2)
     * 2) find the median luminance
     * 3) each pixel that has luminance >= median_luminance will be white changed to white and each pixel
     *      that has luminance < median_luminance will be changed to black
     * @param image image to transform
     * @return black/white stylized form of image
     */
    public static Img ConvertToBW(Img image) {
        ArrayList<Integer> lumiSort = new ArrayList<Integer>();
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        int lumiPix;
        for(int h = 0; h<height; h++){//for each row
            for(int w=0; w<width; w++){//for each pixel
                currentPix = image.GetRGB(w,h);//currentPix holds rgb vals of current pixel
                int r = currentPix.GetRed();//r holds red val of current pixel
                int g = currentPix.GetGreen();//g holds green val of current pixel
                int b = currentPix.GetBlue();//b holds blue val of current pixel
                lumiPix = (int) (.299*(r^2) + .587*(g^2) + .114*(b^2))^(1/2);//using r, g, and b, calculate the luminance of current pixel
                if(!lumiSort.isEmpty()){
                    for(int i = 0; i<lumiSort.size(); i++){
                        if(lumiSort.get(i)>lumiPix){
                            lumiSort.add(i,lumiPix);
                            break;
                        }
                    }
                }
                else{
                    lumiSort.add(lumiPix);
                }
            }
        }
        int lumiMed = lumiSort.get(lumiSort.size()/2);
        RGB black = new RGB();
        RGB white = new RGB (255,255,255);
        for(int h = 0; h<height; h++){//make the sorted arraylist of luminance vals
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                int r = currentPix.GetRed();
                int g = currentPix.GetGreen();
                int b = currentPix.GetBlue();
                lumiPix = (int) (.299*(r^2) + .587*(g^2) + .114*(b^2))^(1/2);
                if(lumiPix>=lumiMed){
                    image.SetRGB(w,h,white);
                }
                else{
                    image.SetRGB(w,h,black);
                }
            }
        }
        return image;
    }

    /**
     * Rotates the image 90 degrees clockwise.
     * @param image image to transform
     * @return image rotated 90 degrees clockwise
     */
    public  static Img RotateImage(Img image) {
        Img turn = new Img(image.GetHeight(),image.GetWidth());
        for(int h=0; h< image.GetHeight(); h++){
            for(int w=0; w<image.GetWidth(); w++){
                RGB currentPix=image.GetRGB(w,h);
                turn.SetRGB(image.GetHeight()-1-h,w,currentPix);
            }
        }
        return turn;
    }

    /**
     * Applies an Instagram-like filter to the image. To do so, we apply the following transformations:
     * 1) We apply a "warm" filter. We can produce warm colors by reducing the amount of blue in the image
     *      and increasing the amount of red. For each pixel, apply the following transformation:
     *          r = r * 1.2
     *          g = g
     *          b = b / 1.5
     * 2) We add a vignette (a black gradient around the border) by combining our image with an
     *      an image of a halo (you can see the image at resources/halo.png). We take 65% of our
     *      image and 35% of the halo image. For example:
     *          r = .65 * r_image + .35 * r_halo
     * 3) We add decorative grain by combining our image with a decorative grain image
     *      (resources/decorative_grain.png). We will do this at a .95 / .5 ratio.
     * @param image image to transform
     * @return image with a filter
     * @throws IOException
     */
    public static Img InstagramFilter(Img image) throws IOException {
        //re: this method
        //since the images we are supposed to be putting on over have different widths/heights as the original image,
        //are we supposed to compress/expand those images to put them over?
        //that is not clearly indicated by the instructions
        //and leaves a ton of variability as to how to do this
        //it also makes it nearly impossible for the method test to run correctly
        //as it is not immediately obvious what procedure was done to the sample image
        //in this case, applying a 602x602 image atop a 980x550 image requires a serious amount of distortion
        //as such, because this process is not specified at all, i will be doubling the images in width
        //so that they are 1204x602, and then cutting them evenly to size
        //this process would not be replicable with any image
        //my apologies
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        Img halo = new Img("C:\\Users\\Angelfish\\IdeaProjects\\project-5-image-manipulation-leileibea\\resources\\halo.png");
        Img grain = new Img("C:\\Users\\Angelfish\\IdeaProjects\\project-5-image-manipulation-leileibea\\resources\\decorative_grain.png");
        //Img halo = new Img("../resources/halo.png");
        //Img grain = new Img("../resources/decorative_grain.png");
        //distort methods
        //width multiplier check
        Img haloNew = new Img(halo.GetWidth(), halo.GetHeight());
        Img grainNew = new Img(grain.GetWidth(), grain.GetHeight());
        //warm
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                int r = currentPix.GetRed();
                int g = currentPix.GetGreen();
                int b = currentPix.GetBlue();
                currentPix.SetRed((int)(1.2*r));
                currentPix.SetGreen(g);
                currentPix.SetBlue(b/2);
                image.SetRGB(w,h,currentPix);
            }
        }
        //vignette
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                RGB vignettePix = halo.GetRGB(w,h);
                int r = currentPix.GetRed();
                int g = currentPix.GetGreen();
                int b = currentPix.GetBlue();
                int rVig = vignettePix.GetRed();
                int gVig = vignettePix.GetGreen();
                int bVig = vignettePix.GetBlue();
                currentPix.SetRed((int)(.65*r + .35*rVig));
                currentPix.SetGreen((int)(.65*g + .35*gVig));
                currentPix.SetBlue((int)(.65*b + .35*bVig));
                image.SetRGB(w,h,currentPix);
            }
        }
        //grain
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                RGB grainPix = grain.GetRGB(w,h);
                int r = currentPix.GetRed();
                int g = currentPix.GetGreen();
                int b = currentPix.GetBlue();
                int rG = grainPix.GetRed();
                int gG = grainPix.GetGreen();
                int bG = grainPix.GetBlue();
                currentPix.SetRed((int)(.95*r + .05*rG));
                currentPix.SetGreen((int)(.95*g + .05*gG));
                currentPix.SetBlue((int)(.95*b + .05*bG));
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }

    /**
     * Sets the given hue to each pixel image. Hue can range from 0 to 360. We do this
     * by converting each RGB pixel to an HSL pixel, Setting the new hue, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param hue amount of hue to add
     * @return image with added hue
     */
    public static Img SetHue(Img image, int hue) {
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                HSL pixHSL = currentPix.ConvertToHSL();
                pixHSL.SetHue(hue);
                currentPix = pixHSL.GetRGB();
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }

    /**
     * Sets the given saturation to the image. Saturation can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new saturation, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param saturation amount of saturation to add
     * @return image with added hue
     */
    public static Img SetSaturation(Img image, double saturation) {
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                HSL pixHSL = currentPix.ConvertToHSL();
                pixHSL.SetSaturation(saturation);
                currentPix = pixHSL.GetRGB();
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }

    /**
     * Sets the lightness to the image. Lightness can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new lightness, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param lightness amount of hue to add
     * @return image with added hue
     */
    public static Img SetLightness(Img image, double lightness) {
        int height = image.GetHeight();
        int width = image.GetWidth();
        RGB currentPix;
        for(int h = 0; h<height; h++){
            for(int w=0; w<width; w++){
                currentPix = image.GetRGB(w,h);
                HSL pixHSL = currentPix.ConvertToHSL();
                pixHSL.SetLightness(lightness);
                currentPix = pixHSL.GetRGB();
                image.SetRGB(w,h,currentPix);
            }
        }
        return image;
    }
}
