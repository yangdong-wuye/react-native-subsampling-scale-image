# react-native-long-image

[subsampling-scale-image-view](https://github.com/davemorrissey/subsampling-scale-image-view) react-native版，旨在解决android加载长图模糊问题

## Preview
![预览](https://github.com/yangdong-wuye/react-native-subsampling-scale-image/tree/master/doc/1653470214074879.gif?raw=true)

## Installation

npm install @wuye/react-native-subsampling-scale-image
yarn add @wuye/react-native-subsampling-scale-image
```

## Usage

```js
import { SubsamplingScaleImage } from "@wuye/react-native-subsampling-scale-image";

// ...

<SubsamplingScaleImage
  zoomEnabled
  panEnabled={true}
  quickScaleEnabled
  maxScale={3}
  minScale={1}
  doubleTapZoomStyle={DoubleTapZoomStyle.ZOOM_FOCUS_FIXED}
  source={{ uri: props.uri }}
  onLoadStart={() => console.log('onLoadStart')}
  onLoad={event => console.log('onLoad', event.nativeEvent)}
  onLoadEnd={() => console.log('onLoadEnd')}
  onLoadCleared={() => console.log('onLoadCleared')}
  onError={() => console.log('onError')}
  onScaleChanged={event =>
    console.log('onScaleChanged', event.nativeEvent)
  }
  onCenterChanged={event =>
    console.log('onCenterChanged', event.nativeEvent)
  }
  style={{
    width: props.width,
    height: props.height,
    overflow: 'visible',
  }}
/>
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
