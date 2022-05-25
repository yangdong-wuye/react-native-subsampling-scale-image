import React, { Fragment } from 'react';
import {
  Image,
  ImageSourcePropType,
  Platform,
  requireNativeComponent,
  StyleProp,
  StyleSheet,
  View,
  ViewStyle,
} from 'react-native';

export enum MinimumScaleType {
  SCALE_TYPE_CENTER_INSIDE = 1,
  SCALE_TYPE_CENTER_CROP = 2,
  SCALE_TYPE_CUSTOM = 3,
  SCALE_TYPE_START = 4,
}

export enum DoubleTapZoomStyle {
  ZOOM_FOCUS_FIXED = 1,
  ZOOM_FOCUS_CENTER = 2,
  ZOOM_FOCUS_CENTER_IMMEDIATE = 3,
}

export interface OnLoadEvent {
  nativeEvent: {
    width: number;
    height: number;
  };
}

export interface OnScaleChangedEvent {
  nativeEvent: {
    newScale: number;
    origin: number;
  };
}

export interface OnCenterChangedEvent {
  nativeEvent: {
    newCenter: { x: number; y: number };
    origin: number;
  };
}

export interface SubsamplingScaleImageProps {
  style?: StyleProp<ViewStyle>;
  source: ImageSourcePropType;
  quickScaleEnabled?: boolean;
  zoomEnabled?: boolean;
  panEnabled?: boolean;
  eagerLoadingEnabled?: boolean;
  minScale?: number;
  maxScale?: number;
  animateScale?: number;
  animateCenter?: { x: number; y: number };
  animateScaleAndCenter?: {
    animateScale: number;
    animateCenter: { x: number; y: number };
  };
  doubleTapZoomDpi?: number;
  doubleTapZoomDuration?: number;
  doubleTapZoomStyle?: DoubleTapZoomStyle;
  minimumScaleType?: MinimumScaleType;
  onLoadStart?: () => void;
  onError?: () => void;
  onLoad?: (event: OnLoadEvent) => void;
  onLoadEnd?: () => void;
  onLoadCleared?: () => void;
  onScaleChanged?: (event: OnScaleChangedEvent) => void;
  onCenterChanged?: (event: OnCenterChangedEvent) => void;
  children?: React.ReactNode;
}

const SubsamplingScaleImageView: any =
  Platform.OS === 'android'
    ? requireNativeComponent('SubsamplingScaleImageView')
    : null;

export const SubsamplingScaleImage = React.forwardRef(
  (props: SubsamplingScaleImageProps, ref: any) => {
    const {
      style,
      source,
      onLoadStart,
      onLoad,
      onLoadEnd,
      onError,
      onLoadCleared,
      onScaleChanged,
      onCenterChanged,
      children,
      ...otherProps
    } = props;

    const _onLoadStart = () => !!onLoadStart && onLoadStart();
    const _onError = () => !!onError && onError();
    const _onLoad = (event: OnLoadEvent) => !!onLoad && onLoad(event);
    const _onLoadEnd = () => !!onLoadEnd && onLoadEnd();
    const _onLoadCleared = () => !!onLoadCleared && onLoadCleared();
    const _onScaleChanged = (event: OnScaleChangedEvent) =>
      !!onScaleChanged && onScaleChanged(event);
    const _onCenterChanged = (event: OnCenterChangedEvent) =>
      !!onCenterChanged && onCenterChanged(event);

    const resolveAssetSource = Image.resolveAssetSource(source);

    return (
      <View ref={ref} style={[styles.imageContainer, style]}>
        <Fragment>
          <SubsamplingScaleImageView
            style={StyleSheet.absoluteFill}
            source={resolveAssetSource}
            onSubsamplingScaleImageLoadStart={_onLoadStart}
            onSubsamplingScaleImageLoadError={_onError}
            onSubsamplingScaleImageLoad={_onLoad}
            onSubsamplingScaleImageLoadEnd={_onLoadEnd}
            onSubsamplingScaleImageLoadCleared={_onLoadCleared}
            onSubsamplingScaleImageScaleChanged={_onScaleChanged}
            onSubsamplingScaleImageCenterChanged={_onCenterChanged}
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
