## Android Gallery

![](./art/Gallery.png)


## Usage


```kotlin
GalleryActivity.Builder()
        .choose(MimeType.ALL)                           
        .multiple(true)     
        .maxSelect(5)               
        .imageMaxSize(1024)         
        .videoMaxSecond(60)          
        .videoMinSecond(3)
        .forResult(this, REQUEST_SELECT_MEDIA)
```

## Get Selected Media


https://github.com/zhihu/Matisse
https://github.com/HuanTanSheng/EasyPhotos
https://github.com/LuckSiege/PictureSelector
https://github.com/jkwiecien/EasyImage
