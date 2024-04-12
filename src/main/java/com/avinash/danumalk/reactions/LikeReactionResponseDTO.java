package com.avinash.danumalk.reactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeReactionResponseDTO {
    private Long likeId;

    private Integer userId;


}
