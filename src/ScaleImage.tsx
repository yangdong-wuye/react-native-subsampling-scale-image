import React, { Fragment } from 'react';
import {
  Image,
  Platform,
  requireNativeComponent,
  StyleSheet,
  View,
} from 'react-native';
import type { OnLoadEvent, ScaleImageProps } from './types';

const ScaleImageView: any =
  Platform.OS === 'android' ? requireNativeComponent('ScaleImageView') : null;

export const ScaleImage = React.forwardRef(
  (props: ScaleImageProps, ref: any) => {
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

    const resolveAssetSource = Image.resolveAssetSource(source);

    return (
      <View ref={ref} style={[styles.imageContainer, style]}>
        <Fragment>
          <ScaleImageView
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
  }
);

const styles = StyleSheet.create({
  imageContainer: {
    overflow: 'hidden',
  },
});
