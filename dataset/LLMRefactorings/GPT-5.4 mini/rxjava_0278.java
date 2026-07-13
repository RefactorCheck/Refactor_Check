public class rxjava_0278 {

        @CheckReturnValue
        @NonNull
        static @SchedulerSupport(SchedulerSupport.NONE)
        public static <@NonNull T1, @NonNull T2, @NonNull T3, @NonNull T4, @NonNull T5, @NonNull T6, @NonNull T7, @NonNull T8, @NonNull T9, @NonNull R> Maybe<R> zip(
                @NonNull MaybeSource<? extends T1> source1, @NonNull MaybeSource<? extends T2> source2, @NonNull MaybeSource<? extends T3> source3,
                @NonNull MaybeSource<? extends T4> source4, @NonNull MaybeSource<? extends T5> source5, @NonNull MaybeSource<? extends T6> source6,
                @NonNull MaybeSource<? extends T7> source7, @NonNull MaybeSource<? extends T8> source8, @NonNull MaybeSource<? extends T9> source9,
                @NonNull Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
    
            Objects.requireNonNull(source1, "source1 is null");
            Objects.requireNonNull(source2, "source2 is null");
            Objects.requireNonNull(source3, "source3 is null");
            Objects.requireNonNull(source4, "source4 is null");
            Objects.requireNonNull(source5, "source5 is null");
            Objects.requireNonNull(source6, "source6 is null");
            Objects.requireNonNull(source7, "source7 is null");
            Objects.requireNonNull(source8, "source8 is null");
            Objects.requireNonNull(source9, "source9 is null");
            Objects.requireNonNull(zipper, "zipper is null");
            return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8, source9);
        }
}
