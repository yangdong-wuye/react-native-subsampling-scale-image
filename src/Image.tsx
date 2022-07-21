import React, { Fragment } from 'react';
import {
  Image as RNImage,
  Platform,
  requireNativeComponent,
  StyleSheet,
  View,
} from 'react-native';
import type { ImageProps, OnLoadEvent } from './types';

const ImageView: any =
  Platform.OS === 'android' ? requireNativeComponent('ImageView') : null;

export const Image = React.forwardRef((props: ImageProps, ref: any) => {
  const {
    style,
    source,
    onLoadStart,
    onLoad,
    onLoadEnd,
    onError,
    onLoadCleared,
    children,
    ...otherProps
  } = props;

  const _onLoadStart = () => !!onLoadStart && onLoadStart();
  const _onError = () => !!onError && onError();
  const _onLoad = (event: OnLoadEvent) => !!onLoad && onLoad(event);
  const _onLoadEnd = () => !!onLoadEnd && onLoadEnd();
  const _onLoadCleared = () => !!onLoadCleared && onLoadCleared();

  const resolveAssetSource = RNImage.resolveAssetSource(source);

  return (
    <View ref={ref} style={[styles.imageContainer, style]}>
      <Fragment>
        <ImageView
          style={StyleSheet.absoluteFill}
          source={resolveAssetSource}
          onScaleImageLoadStart={_onLoadStart}
          onScaleImageLoadError={_onError}
          onScaleImageLoad={_onLoad}
          onScaleImageLoadEnd={_onLoadEnd}
          onScaleImageLoadCleared={_onLoadCleared}
          {...otherProps}
        />
        {children}
      </Fragment>
    </View>
  );
});

const styles = StyleSheet.create({
  imageContainer: {
    overflow: 'hidden',
  },
});
