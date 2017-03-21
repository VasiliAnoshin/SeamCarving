# SeamCarving
Algorithm for image resizing : It functions by establishing a number of seams (paths of least importance) in an image and automatically removes seams to reduce image size or inserts seams to extend it <br />
![lena](https://cloud.githubusercontent.com/assets/9945039/24169613/d7918250-0e86-11e7-8d88-042d8a82261e.jpg)<br />
GrayScale: Convert the image to grayscale using by average between 3 colors or by formula .
 <br />
![lenaingray](https://cloud.githubusercontent.com/assets/9945039/24170027/21f68b3c-0e88-11e7-93ec-c100b8a0c1f4.jpg)<br />
Gradient: The gradient magnitude can be computed as √𝑑𝑥^2 + 𝑑𝑦^2, where 𝑑𝑥 is the
difference between the current and next horizontal pixel, and 𝑑𝑦 is the difference
between the current and next vertical pixel. Border conditions can be ignored (just put zero or trim).<br />
![lenainblack](https://cloud.githubusercontent.com/assets/9945039/24170207/ca0f16cc-0e88-11e7-9fa0-35d2490a0e2f.jpg)<br />
<H4>SEAMS:</H4> <br />

Seams can be either vertical or horizontal. A vertical seam is a path of pixels connected from top to bottom in an image with one pixel in each row. A horizontal seam is similar with the exception of the connection being from left to right. The importance/energy function values a pixel by measuring its contrast with its neighbor pixels.
Computing the seam consists of finding the path of minimum energy cost from one end of the image to another. This is done by Dynamic programming.<br />
Algorithm : <br />
We Start with an image of Lena. We then calculate the weight/density/energy of each pixel. This can be done by gradient.
After we have the energy of the image, we generate a list of seams: <br/>
![reatgetbyhorizont](https://cloud.githubusercontent.com/assets/9945039/24171621/e217c7dc-0e8d-11e7-94e3-9705e8ecad6b.jpg)<br/>
We then remove the seams from the image, reducing the size of the image as a result:
![afterretargeting](https://cloud.githubusercontent.com/assets/9945039/24172720/e4cd948a-0e91-11e7-9fc3-889761231d9b.jpg)
