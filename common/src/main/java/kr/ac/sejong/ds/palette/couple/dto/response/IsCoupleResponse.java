package kr.ac.sejong.ds.palette.couple.dto.response;

public record IsCoupleResponse (
        Boolean isCouple
) {
    public static IsCoupleResponse of(Boolean isCouple) {
        return new IsCoupleResponse(isCouple);
    }
}
